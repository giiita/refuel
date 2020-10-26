package refuel.session

import java.util.UUID

import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject
import refuel.AkkaHttpWebContext.SessionId

@Inject(Finally)
class SessionIDGenerator() extends AutoInject {
  def gen: SessionId = SessionId(UUID.randomUUID().toString)
}
