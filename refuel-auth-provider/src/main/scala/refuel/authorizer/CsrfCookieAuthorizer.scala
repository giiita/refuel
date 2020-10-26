package refuel.authorizer

import java.util.UUID

import org.pac4j.core.context.Cookie
import refuel.AkkaHttpWebContext
import refuel.saml.SAMLAuthConfig

object CsrfCookieAuthorizer {

  def setup(context: AkkaHttpWebContext)(
      implicit conf: SAMLAuthConfig
  ): AkkaHttpWebContext = {
    val token  = UUID.randomUUID.toString
    val cookie = new Cookie(conf.csrfTokenKey, token)
    cookie.setPath(conf.cookiePath)

    cookie.setMaxAge(conf.lifetimeSeconds.toInt)

    context.setRequestAttribute(conf.csrfTokenKey, token)
    context.getSessionStore.set(context, conf.csrfTokenKey, token)
    context.addResponseCookie(cookie)

    context
  }

}
