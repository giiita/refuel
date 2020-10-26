package refuel.store

import refuel.AkkaHttpWebContext.SessionId
import SessionStorage.ValueKey

class ForgetfulSessionStorage extends SessionStorage {

  override def createOrReuseSession(sessionId: SessionId): Boolean = true

  override def sessionExists(sessionId: SessionId): Boolean = false

  override def getSessionValue(sessionId: SessionId, key: ValueKey): Option[Any] = None

  override def setSessionValue(sessionId: SessionId, key: ValueKey, value: Any): Boolean = false

  override def destroySession(sessionId: SessionId): Boolean = false

  override def renewSession(sessionId: SessionId): Boolean = false

  override def getSessionValues(sessionId: SessionId): Option[Map[ValueKey, Any]] = None

  override def setSessionValues(sessionId: SessionId, values: Map[ValueKey, Any]): Boolean = false
}
