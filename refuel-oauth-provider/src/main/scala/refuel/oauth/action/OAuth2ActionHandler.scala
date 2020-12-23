package refuel.oauth.action

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject

@Inject(Finally)
class OAuth2ActionHandler extends AutoInject {
  def oauthActionComplete(action: HttpAction): ToResponseMarshallable = action.result
}
