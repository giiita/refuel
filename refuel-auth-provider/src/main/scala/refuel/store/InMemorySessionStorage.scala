package refuel.store

import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject
import refuel.lang.ScalaTime
import refuel.AkkaHttpWebContext.SessionId
import SessionStorage.ValueKey
import refuel.saml.SAMLAuthConfig

import scala.collection.SortedSet

object InMemorySessionStorage {
  private[store] type RegisteredMs = Long
  private[store] case class ExpiryRecord(registered: RegisteredMs, id: SessionId)
  private[store] case class DataRecord(registered: RegisteredMs, data: Map[ValueKey, Any])
}

/** Build server instance-specific session storage. This is not available in a redundant system, as the same session store must be referenced from all instances.
  *
  * @param st Specified system clock.
  */
@Inject(Finally)
class InMemorySessionStorage(st: ScalaTime, conf: SAMLAuthConfig) extends SessionStorage with AutoInject {

  import InMemorySessionStorage._

  private[this] lazy final val SessionLifetimeMs = conf.lifetimeSeconds

  @volatile private[refuel] var expiryQueue = SortedSet[ExpiryRecord]()(Ordering.by(v => (v.registered, v.id.value)))
  @volatile private[refuel] var sessionData = Map[SessionId, DataRecord]()

  /** Ensure existance of a session with the provided key. Returns whether the session is new
    *
    * @param SessionId
    * @return Returns true when it is newly created.
    */
  override def createOrReuseSession(SessionId: SessionId): Boolean = {
    expireOldSessions()
    sessionData
      .get(SessionId)
      .fold {
        val sessionTime = getTime
        expiryQueue = expiryQueue + ExpiryRecord(sessionTime, SessionId)
        sessionData = sessionData + (SessionId -> DataRecord(sessionTime, Map.empty))
        true
      }(_ => false)
  }

  /** Check whether a session exists
    *
    * @param SessionId
    * @return Returns true if there is a session with the same id.
    */
  override def sessionExists(SessionId: SessionId): Boolean = {
    expireOldSessions()
    sessionData.contains(SessionId)
  }

  /** Get the additional attributes of the session.
    *
    * @param SessionId
    * @param key
    * @return
    */
  override def getSessionValue(SessionId: SessionId, key: ValueKey): Option[Any] = {
    expireOldSessions()
    sessionData.get(SessionId).flatMap(_.data.get(key))
  }

  /** Get the additional attributes of the session.
    *
    * @param SessionId
    * @return
    */
  override def getSessionValues(SessionId: SessionId): Option[Map[ValueKey, Any]] = {
    expireOldSessions()
    sessionData.get(SessionId).map(_.data)
  }

  /** Destroy expired sessions in the local session store.
    */
  private[this] def expireOldSessions(): Unit = {
    val expireFrom = getTime - SessionLifetimeMs

    val (expired, nonExpired) = expiryQueue.span(_.registered < expireFrom)
    sessionData = sessionData -- expired.toSeq.map(_.id)
    expiryQueue = nonExpired
  }

  private[refuel] def getTime: Long = st.now.toEpochSecond

  /** Add the additional attributes of the session.
    *
    * @param SessionId
    * @return Complete status.
    */
  override def setSessionValue(SessionId: SessionId, key: ValueKey, value: Any): Boolean = {
    expireOldSessions()
    sessionData.get(SessionId).fold(false) {
      case DataRecord(registered, data) =>
        sessionData = sessionData + (SessionId -> DataRecord(registered, data + (key -> value)))
        true
    }
  }

  /** Add the additional attributes of the session.
    *
    * @param SessionId
    * @return Complete status.
    */
  override def setSessionValues(SessionId: SessionId, values: Map[ValueKey, Any]): Boolean = {
    expireOldSessions()
    sessionData.get(SessionId).fold(false) {
      case DataRecord(registered, data) =>
        sessionData = sessionData + (SessionId -> DataRecord(registered, data ++ values))
        true
    }
  }

  /** Destroy the session.
    *
    * @param sessionId
    * @return Returns false if the Session did not originally exist.
    */
  override def destroySession(sessionId: SessionId): Boolean = {
    expireOldSessions()
    sessionData.get(sessionId).fold(false) { data =>
      expiryQueue = expiryQueue - ExpiryRecord(data.registered, sessionId)
      sessionData = sessionData - sessionId
      true
    }
  }

  /** Update your session. If it doesn't exist to begin with, nothing is done and it responds false.
    *
    * @param SessionId
    * @return
    */
  override def renewSession(SessionId: SessionId): Boolean = {
    expireOldSessions()
    sessionData.get(SessionId).fold(false) {
      case DataRecord(registered, data) =>
        val now = getTime
        sessionData = sessionData + (SessionId -> DataRecord(now, data))
        expiryQueue = (expiryQueue - ExpiryRecord(registered, SessionId)) + ExpiryRecord(now, SessionId)
        true
    }
  }
}
