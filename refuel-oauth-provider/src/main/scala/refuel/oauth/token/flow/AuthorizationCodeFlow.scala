package refuel.oauth.token.flow

import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.oauth.action.{HttpAction, OAuth2ActionHandler}
import refuel.oauth.authorize.{AuthProfile, AuthorizeState}
import refuel.oauth.client.AuthorizedApp
import refuel.oauth.exception.InvalidGrantException
import refuel.oauth.grant.GrantHandler
import refuel.oauth.token.GrantFlow
import refuel.oauth.token.GrantRequest.AuthorizeCodeGrantRequest

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Inject[Finally]
class AuthorizationCodeFlow(override val actionHandler: OAuth2ActionHandler)(implicit ec: ExecutionContext)
    extends GrantFlow[Future, AuthorizeCodeGrantRequest]
    with Directives {
  override def sync[U](
      v1: AuthorizeCodeGrantRequest,
      grantHandler: GrantHandler[Future, U]
  ): Directive1[AuthProfile[U]] =
    onComplete[AuthProfile[U]] {
      for {
        app   <- grantHandler.findApp(v1.clientId)
        _     <- Future.fromTry(grantVerification(v1, app))
        state <- grantHandler.restoreAuthorization(v1.code)
        _     <- Future.fromTry(grantVerification(v1, state))
      } yield state
    }

  private[flow] def grantVerification(req: AuthorizeCodeGrantRequest, app: AuthorizedApp): Try[Int] = {
    appVerification(req, app).map(_ => 1)
  }

  private[flow] def grantVerification[U](req: AuthorizeCodeGrantRequest, state: AuthorizeState[U]): Try[Int] = {
    Either
      .cond(
        state.codeChallenge match {
          case None =>
            req.codeVerifier.isEmpty
          case Some(x) =>
            req.codeVerifier.fold(false)(x.verify)
        },
        1,
        new InvalidGrantException("Challenge code verification failed.")
      )
      .flatMap { _ =>
        Either.cond(state.redirectUri == req.redirectUri, 1, new InvalidGrantException("Invalid redirection."))
      }
      .toTry
  }
}
