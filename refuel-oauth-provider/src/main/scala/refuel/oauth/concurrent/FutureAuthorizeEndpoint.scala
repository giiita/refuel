package refuel.oauth.concurrent

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{HttpResponse, StatusCodes, Uri}
import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.injector.AutoInject
import refuel.oauth.action.{HttpAction, OAuth2ActionHandler}
import refuel.oauth.authorize.AuthorizeRequest.{AuthorizationCodeRequest, ImplicitGrantRequest}
import refuel.oauth.authorize.{AuthorizeCode, AuthorizeRequest, CodeChallenge, ResponseType}
import refuel.oauth.endpoint.AuthorizeEndpoint
import refuel.oauth.exception.{
  AuthorizeChallengeException,
  AuthorizeChallengeFailed,
  AuthorizeErrorConstant,
  InvalidGrantException,
  RequirementFailedException
}
import refuel.oauth.grant.GrantHandler
import refuel.oauth.token.AccessToken

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class FutureAuthorizeEndpoint(actionHandler: OAuth2ActionHandler)(implicit ec: ExecutionContext)
    extends Directives
    with AuthorizeEndpoint[Future]
    with AutoInject {

  /** Authorize step 1
    *
    * Minimally inspect authorization requests.
    *
    * When an invalid authorization request is detected, if we know the redirect endpoint of
    * the service provider, we will redirect complete with an error code, but if we don't know
    * it, this endpoint will respond with an abnormal value such as 401.
    *
    * It is a directive that only checks the legitimacy of the authorization request. After this, it is
    * necessary to check any authentication and authorization information and redirect to the authorization screen
    * of the authorization server or to the redirect uri of the service provider, if necessary.
    *
    * Since the specific authentication and authorization algorithms from here on are not strictly
    * defined in OAuth2.0, an original implementation is required. However, it is possible to use `AAA` for
    * the redirect to the service provider after the authorization is completed.
    *
    */
  def validAuthorizeRequest(
      parameters: Map[String, String],
      grantHandler: GrantHandler[Future, _]
  ): Directive1[AuthorizeRequest] = {
    onComplete(buildRequest(parameters, grantHandler)).flatMap {
      case Success(request) => provide(request)
      case Failure(e)       => complete(actionHandler.oauthActionComplete(HttpAction(e)))
    }
  }

  /** Authorization step 2
    *
    * When the authorization is complete, the authorization request has been stored, and the authorization code
    * has been issued, the service provider must be notified of the authorization code.
    *
    * {{{
    *   path("authorize_endpoint") {
    *     validAuthorizeRequest { req =>
    *       grantAndIssueCode { code =>
    *         authorizeComplete(req, code)
    *       }
    *     }
    *   }
    * }}}
    *
    * @param request Valid authorization request
    * @param authorizeCode Issued authorize code.
    * @return
    */
  def authorizeComplete(request: AuthorizeRequest, authorizeCode: AuthorizeCode): ToResponseMarshallable = {
    authFinalize(request) {
      case req: AuthorizationCodeRequest =>
        HttpResponse(
          status = StatusCodes.Found,
          headers = scala.collection.immutable.Seq(
            Location(
              req.redirectUri
                .withQuery(
                  Query(
                    Map("code" -> authorizeCode.v) ++ req.state.map("state".->) ++ req.redirectUri.query()
                  )
                )
                .withoutFragment
            )
          )
        )
    }
  }

  /** Complete the authorization request.
    * Redirect to the authorization endpoint with an access token, following the conventions.
    *
    * @param request Valid authorization request
    * @param accessToken Issued access token
    * @return
    */
  def implicitGrantComplete(request: AuthorizeRequest, accessToken: AccessToken): ToResponseMarshallable = {
    authFinalize(request) {
      case req: ImplicitGrantRequest =>
        HttpResponse(
          status = StatusCodes.Found,
          headers = scala.collection.immutable.Seq(
            Location(
              req.redirectUri.withQuery(accessToken)
            )
          )
        )
    }
  }

  def buildRequest(request: Map[String, String], grantHandler: GrantHandler[Future, _]): Future[AuthorizeRequest] = {
    val state       = request.get(FutureAuthorizeEndpoint.Constant.State)
    val redirectFor = request.get(FutureAuthorizeEndpoint.Constant.RedirectUri).map(Uri(_))

    def fail[T](key: String)(implicit rf: Option[Uri]): Future[T] = {
      Future.failed(
        AuthorizeChallengeFailed.build(
          AuthorizeErrorConstant.Errors.InvalidRequest,
          state,
          new RequirementFailedException(s"$key is required but was no found."),
          rf
        )
      )
    }

    def getOrFail(const: String)(implicit rf: Option[Uri] = redirectFor): Future[String] = {
      request
        .get(const)
        .fold[Future[String]](fail(const))(Future(_))
    }

    def getOrRedirect(const: String, rf: Uri): Future[String] = {
      getOrFail(const)(Some(rf))
    }

    {
      for {
        clientId <- getOrFail(FutureAuthorizeEndpoint.Constant.ClientId)
        app      <- grantHandler.findApp(clientId)
        redirect <- Future
          .fromTry(app.verifyRedirection(redirectFor))
          .recoverWith {
            case e =>
              Future.failed(
                AuthorizeChallengeFailed.build(
                  AuthorizeErrorConstant.Errors.InvalidRequest,
                  state,
                  e,
                  None
                )
              )
          }
      } yield {
        for {
          responseType <- getOrRedirect(FutureAuthorizeEndpoint.Constant.ResponseType, redirect).flatMap(x =>
            Future.fromTry(ResponseType.apply(x))
          )
          scopes <- grantHandler.verifyGrantScope(
            request
              .get(FutureAuthorizeEndpoint.Constant.Scope)
              .fold[Iterable[String]](Nil)(_.split("\\s").toSeq)
          )
          state     = request.get(FutureAuthorizeEndpoint.Constant.State)
          challenge <- Future.fromTry(CodeChallenge.fromRequest(request))
        } yield AuthorizeRequest(
          responseType,
          clientId,
          scopes,
          challenge,
          state,
          redirect,
          request
        )
      }.recoverWith {
        case e: AuthorizeChallengeException => Future.failed(e)
        case e =>
          Future.failed(
            AuthorizeChallengeFailed.build(AuthorizeErrorConstant.Errors.InvalidRequest, state, e, Some(redirect))
          )
      }
    }.flatten.recoverWith {
      case e: AuthorizeChallengeException => Future.failed(e)
      case e =>
        Future.failed(
          AuthorizeChallengeFailed.build(AuthorizeErrorConstant.Errors.InvalidRequest, state, e, redirectFor)
        )
    }
  }

  private[this] def authFinalize(
      request: AuthorizeRequest
  )(pf: PartialFunction[AuthorizeRequest, ToResponseMarshallable]): ToResponseMarshallable = {
    pf.lift(request).getOrElse(unsupportedGrantType(request))
  }

  private[concurrent] def unsupportedGrantType(req: AuthorizeRequest): ToResponseMarshallable = {
    actionHandler.oauthActionComplete {
      HttpAction.apply(
        AuthorizeChallengeFailed.build(
          AuthorizeErrorConstant.Errors.InvalidRequest,
          req.state,
          new InvalidGrantException("Unsupported grant type."),
          Some(req.redirectUri)
        )
      )
    }
  }
}

object FutureAuthorizeEndpoint {
  object Constant {
    final lazy val ResponseType        = "response_type"
    final lazy val ClientId            = "client_id"
    final lazy val Scope               = "scope"
    final lazy val State               = "state"
    final lazy val CodeChallenge       = "code_challenge"
    final lazy val CodeChallengeMethod = "code_challenge_method"
    final lazy val RedirectUri         = "redirect_uri"
  }
}
