package refuel.oauth.concurrent

import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.injector.AutoInject
import refuel.oauth.action.{OAuth2ActionHandler, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.endpoint.ProtectionEndpoint
import refuel.oauth.grant.GrantHandler

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class FutureProtectionEndpoint(actionHandler: OAuth2ActionHandler)(implicit ec: ExecutionContext)
    extends Directives
    with ProtectionEndpoint[Future]
    with AutoInject {
  override def verify[U](accessToken: String, grantHandler: GrantHandler[Future, U]): Directive1[AuthProfile[U]] = {
    onComplete(grantHandler.verifyAccessToken(accessToken)).flatMap {
      case Success(e) => provide(e)
      case Failure(e) => complete(actionHandler.oauthActionComplete(UnauthorizedAction(e)))
    }
  }
}
