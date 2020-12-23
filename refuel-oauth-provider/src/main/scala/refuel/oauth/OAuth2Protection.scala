package refuel.oauth

import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.injector.AutoInject
import refuel.oauth.action.{OAuth2ActionHandler, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.endpoint.ProtectionEndpoint
import refuel.oauth.exception.UnauthorizedException
import refuel.oauth.grant.GrantHandler

trait OAuth2Protection[F[_], U] extends Directives with AutoInject {

  // Require override definition
  // class MyProtection(
  //   override val protection: ProtectionEndpoint[Future],
  //   override val grantHandler: GrantHandler[Future, User],
  //   override val actionHandler: OAuth2ActionHandler
  // ) extends OAuth2ProtectedResource[Future, User]
  val protection: ProtectionEndpoint[F]
  val grantHandler: GrantHandler[F, U]
  val actionHandler: OAuth2ActionHandler

  def oauth2: Directive1[AuthProfile[U]] = {
    optionalHeaderValuePF {
      case Authorization(creds) => creds
    }.flatMap[Tuple1[AuthProfile[U]]] {
      case Some(creds: OAuth2BearerToken) =>
        protection.verify(creds.token, grantHandler)
      case _ =>
        complete(
          actionHandler.oauthActionComplete(UnauthorizedAction(new UnauthorizedException("Invalid authorization.")))
        )
    }
  }
}
