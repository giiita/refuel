package refuel.store

import refuel.AkkaHttpWebContext.SessionId

object SessionStorage {
  type ValueKey = String
}

trait SessionStorage {
  import SessionStorage._

  /**
    * Ensure existance of a session with the provided key. Returns whether the session is new
    */
  def createOrReuseSession(sessionId: SessionId): Boolean

  /**
    * Check whether a session exists
    */
  def sessionExists(sessionId: SessionId): Boolean

  /**
    * Get value stored in existing session, returns None when either the session or value cannot be found
    */
  def getSessionValue(sessionId: SessionId, key: ValueKey): Option[Any]

  /**
    * Get all values stored in an existing session, returns None when the session id or the values cannot be found
    */
  def getSessionValues(sessionId: SessionId): Option[Map[ValueKey, Any]]

  /**
    * Set a value for a given session. Returns false if the session did not exist
    */
  def setSessionValue(sessionId: SessionId, key: ValueKey, value: Any): Boolean

  /**
    * Set all values for a given session. Returns false if the session did not exist
    */
  def setSessionValues(sessionId: SessionId, values: Map[ValueKey, Any]): Boolean

  /**
    * Renew a session, meaning its lifetime start from 'now'
    */
  def renewSession(sessionId: SessionId): Boolean

  /**
    * Destroy a session
    */
  def destroySession(sessionId: SessionId): Boolean

}
