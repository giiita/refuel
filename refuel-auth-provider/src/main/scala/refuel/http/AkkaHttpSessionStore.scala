package refuel.http

import java.util.Optional

import org.pac4j.core.context.session.SessionStore
import refuel.AkkaHttpWebContext
import AkkaHttpWebContext.SessionId

object AkkaHttpSessionStore extends SessionStore[AkkaHttpWebContext] {

  /** Get the object from its key in store.
    *
    * @param context the web context
    * @param key     the key of the object
    * @return the optional object in store
    */
  override def get(context: AkkaHttpWebContext, key: String): Optional[Object] =
    context.sessionStorage
      .getSessionValue(context.sessionId, key)
      .map(_.asInstanceOf[Object])
      .fold[Optional[Object]](Optional.empty())(Optional.of)

  /** Save an object in the store by its key.
    *
    * @param context the web context
    * @param key     the key of the object
    * @param value   the value to save in store
    */
  override def set(context: AkkaHttpWebContext, key: String, value: scala.AnyRef): Unit =
    context.sessionStorage.setSessionValue(context.sessionId, key, value)

  /** Get the native session as a trackable object.
    *
    * @param context the web context
    * @return the optional trackable object
    */
  override def getTrackableSession(context: AkkaHttpWebContext): Optional[AnyRef] =
    Optional.of(context.sessionId.value)

  /** Build a new session store from a trackable session.
    *
    * @param context          the web context
    * @param trackableSession the trackable session
    * @return the optional new session store
    */
  override def buildFromTrackableSession(
      context: AkkaHttpWebContext,
      trackableSession: scala.Any
  ): Optional[SessionStore[AkkaHttpWebContext]] = {
    context.trackSession(SessionId(trackableSession.toString))
    Optional.of(this)
  }

  /** Renew the native session by copying all data to a new one.
    *
    * @param context the web context
    * @return whether the session store has renewed the session
    */
  override def renewSession(context: AkkaHttpWebContext): Boolean = {
    val sessionId     = SessionId(getOrCreateSessionId(context))
    val sessionValues = context.sessionStorage.getSessionValues(sessionId)
    destroySession(context)

    val newSessionId = SessionId(getOrCreateSessionId(context))
    sessionValues.foreach(context.sessionStorage.setSessionValues(newSessionId, _))

    true
  }

  /** Get or create the session identifier and initialize the session with it if necessary.
    *
    * @param context the web context
    * @return the session identifier
    */
  override def getOrCreateSessionId(context: AkkaHttpWebContext): String = context.sessionId.value

  /** Destroy the web session.
    *
    * @param context the web context
    * @return whether the session has been destroyed
    */
  override def destroySession(context: AkkaHttpWebContext): Boolean = context.destroySession()
}
