package refuel.oauth.grant

import akka.http.scaladsl.server.Directive1
import refuel.domination.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}
import refuel.oauth.action.{HttpAction, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.endpoint.TokenEndpoint
import refuel.oauth.exception.InvalidGrantException
import refuel.oauth.token.GrantRequest.{
  AuthorizeCodeGrantRequest,
  ClientCredentialsGrantRequest,
  PasswordGrantRequest,
  RefreshTokenGrantRequest
}
import refuel.oauth.token.{GrantFlow, GrantRequest}

import scala.concurrent.Future
import scala.util.{Failure, Try}

trait GrantFlowDispatcher[F[_]] extends AutoInject {

  protected val authorizationCodeFlow: GrantFlow[F, AuthorizeCodeGrantRequest]
  protected val refreshTokenFlow: GrantFlow[F, RefreshTokenGrantRequest]
  protected val passwordFlow: GrantFlow[F, PasswordGrantRequest]
  protected val clientCredentialsFlow: GrantFlow[F, ClientCredentialsGrantRequest]

  def dispatch[U](
      req: GrantRequest,
      handler: GrantHandler[F, U]
  ): Either[HttpAction, Directive1[AuthProfile[U]]] = {
    req
      .require(TokenEndpoint.Constant.GrantType)
      .toTry
      .flatMap(
        applyFlow(req, handler)
          .lift(_)
          .fold[Try[Directive1[AuthProfile[U]]]](Failure(new InvalidGrantException("Unsupported grant type.")))(x => x)
      )
  }.fold(
    e => Left(UnauthorizedAction(e)),
    Right(_)
  )

  /** It is possible to define extended grant types.
    *
    * @param req
    * @param handler
    * @tparam U
    * @return
    */
  def applyFlow[U](
      req: GrantRequest,
      handler: GrantHandler[F, U]
  ): PartialFunction[String, Try[Directive1[AuthProfile[U]]]] = {
    case "authorization_code" =>
      AuthorizeCodeGrantRequest.apply(req).map(authorizationCodeFlow.sync(_, handler))
    case "refresh_token" =>
      RefreshTokenGrantRequest.apply(req).map(refreshTokenFlow.sync(_, handler))
    case "password" =>
      PasswordGrantRequest.apply(req).map(passwordFlow.sync(_, handler))
    case "client_credentials" =>
      ClientCredentialsGrantRequest.apply(req).map(clientCredentialsFlow.sync(_, handler))
  }
}

@Inject[Finally]
class DefaultGrantFlowDispatcher(
    override val authorizationCodeFlow: GrantFlow[Future, AuthorizeCodeGrantRequest],
    override val refreshTokenFlow: GrantFlow[Future, RefreshTokenGrantRequest],
    override val passwordFlow: GrantFlow[Future, PasswordGrantRequest],
    override val clientCredentialsFlow: GrantFlow[Future, ClientCredentialsGrantRequest]
) extends GrantFlowDispatcher[Future]
