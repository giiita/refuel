package refuel.oauth.token

import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.inject.AutoInject
import refuel.oauth.action.{OAuth2ActionHandler, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.client.AuthorizedApp
import refuel.oauth.exception.AuthorizeChallengeException
import refuel.oauth.grant.GrantHandler

import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}

trait GrantFlow[F[_], -T <: GrantRequest] extends Directives with AutoInject {

  val actionHandler: OAuth2ActionHandler

  def sync[U](
      v1: T,
      grantHandler: GrantHandler[F, U]
  ): Directive1[AuthProfile[U]]

  def appVerification(req: GrantRequest, app: AuthorizedApp): Try[Int] = {
    for {
      _ <- Either
        .cond(app.clientSecret == req.clientSecret, 1, new AuthorizeChallengeException("Invalid grant request."))
        .toTry
    } yield 1
  }

  protected implicit def AuthorizationFoldComplete[U](v: Directive1[Try[AuthProfile[U]]]): Directive1[AuthProfile[U]] =
    v.flatMap {
      case Success(x) => provide(x)
      case Failure(e) => complete(actionHandler.oauthActionComplete(UnauthorizedAction(e)))
    }
}
