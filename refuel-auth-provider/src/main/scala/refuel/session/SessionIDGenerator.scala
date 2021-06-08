package refuel.session

import java.util.UUID
import refuel.domination.InjectionPriority.Finally
import refuel.AkkaHttpWebContext.SessionId
import refuel.inject.{AutoInject, Inject}

@Inject[Finally]
class SessionIDGenerator() extends AutoInject {
  def gen: SessionId = SessionId(UUID.randomUUID().toString)
}
