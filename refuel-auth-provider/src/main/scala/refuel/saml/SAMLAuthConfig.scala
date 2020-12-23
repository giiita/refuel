package refuel.saml

import org.opensaml.saml.common.xml.SAMLConstants
import org.pac4j.core.util.Pac4jConstants
import refuel.injector.AutoInject

/** By default, it injects the configuration model with application.conf loaded.
  *
  * @param keystorePath SAML keystore path
  * @param keystorePassword SAML keystore password
  * @param privateKeyPassword Private key password
  * @param idpMetadataPath Identity provider metadata file
  * @param spMetadataPath Service provider metadata file
  * @param authnRequestBindingType AuthnRequest binding type
  * @param callbackurl SAML callback url
  * @param sessionCookieName SESSION cookie name
  * @param lifetimeSeconds Cookie lifetime seconds.
  * @param cookiePath Cookie path
  * @param cookieSecure Cookie secure mode.
  *                     If not specified, check if the request uri scheme is https.
  *                     In the case of SSL L4 termination, the client may not have the Secure attribute even though it is supposed to be communicating over SSL. Therefore, set True when enforcing Secure
  * @param cookieDomain Cookie domain
  * @param cookieExtension Cookie extension.
  *                        For use with the REST API, the SameSite=None; attribute and https communication are mandatory..
  * @param forceRedirectScheme For SSL L4 termination, to prevent that when the web app receives a request via http, the redirect URI is also http.
  *                            Basically, https is required because SameSite=None; is required, but it is optional because you may test with http for verification.
  * @param csrfTokenKey CSRF token cookie name
  */
case class SAMLAuthConfig(
    // Create automatically if it doesn't exist and you have write access
    keystorePath: String,
    keystorePassword: String,
    privateKeyPassword: String,
    // You need to download the metadata from the identity provider
    idpMetadataPath: String,
    spMetadataPath: Option[String],
    authnRequestBindingType: String = SAMLConstants.SAML2_POST_BINDING_URI,
    callbackurl: String,
    sessionCookieName: String = Pac4jConstants.SESSION_ID,
    lifetimeSeconds: Long = 86400 * 3,
    // CookieSetting
    cookiePath: String = Pac4jConstants.DEFAULT_URL_VALUE,
    cookieSecure: Option[Boolean] = Some(true),
    cookieDomain: Option[String] = None,
    cookieExtension: Option[String] = Some("SameSite=None"),
    forceRedirectScheme: Option[String] = Some("https"),
    csrfTokenKey: String = Pac4jConstants.CSRF_TOKEN
)

object SAMLAuthConfig {
  import pureconfig._
  import generic.auto._

  val xxx = ConfigSource.default.value()

  private[this] lazy final val conf: SAMLAuthConfigWrapper = ConfigSource.default
    .load[SAMLAuthConfigWrapper]
    .fold(
      { e =>
        throw new IllegalArgumentException(
          s"The application configuration is invalid. ${e.toList.map(_.description).mkString(" & ")}"
        )
      }, { x => x }
    )

  case class SAMLAuthConfigWrapper(saml: SAMLAuthConfig)

  class SAMLAuthConfigImpl
      extends SAMLAuthConfig(
        conf.saml.keystorePath,
        conf.saml.keystorePassword,
        conf.saml.privateKeyPassword,
        conf.saml.idpMetadataPath,
        conf.saml.spMetadataPath,
        conf.saml.authnRequestBindingType,
        conf.saml.callbackurl,
        conf.saml.sessionCookieName,
        conf.saml.lifetimeSeconds,
        conf.saml.cookiePath,
        conf.saml.cookieSecure,
        conf.saml.cookieDomain,
        conf.saml.cookieExtension,
        conf.saml.forceRedirectScheme,
        conf.saml.csrfTokenKey
      )
      with AutoInject
}
