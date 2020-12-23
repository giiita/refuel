package refuel.oauth.concurrent

import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.oauth.action.{OAuth2ActionHandler, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.endpoint.TokenEndpoint
import refuel.oauth.exception.UnauthorizedException
import refuel.oauth.grant.{GrantFlowDispatcher, GrantHandler}
import refuel.oauth.token.GrantRequest.Raw

import scala.concurrent.Future

class FutureTokenEndpoint(dispatcher: GrantFlowDispatcher[Future], actionHandler: OAuth2ActionHandler)
    extends TokenEndpoint[Future]
    with Directives {
  def grant[U](grantHandler: GrantHandler[Future, U]): Directive1[AuthProfile[U]] = {
    optionalHeaderValuePF {
      case Authorization(creds) => creds
    }.flatMap[Tuple1[AuthProfile[U]]] {
      case Some(creds: BasicHttpCredentials) =>
        formFieldMap
          .map(Raw(creds, _))
          .flatMap(r =>
            dispatcher.dispatch(r, grantHandler) match {
              case Right(d)     => d.tmap(x => x)
              case Left(action) => complete(actionHandler.oauthActionComplete(action))
            }
          )
      case _ =>
        complete(
          actionHandler.oauthActionComplete(UnauthorizedAction(new UnauthorizedException("Invalid authorize request.")))
        )
    }
  }
}
