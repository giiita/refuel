package refuel.oauth.action

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import refuel.domination.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

@Inject[Finally]
class OAuth2ActionHandler extends AutoInject {
  def oauthActionComplete(action: HttpAction): ToResponseMarshallable = action.result
}
