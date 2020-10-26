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
    csrfTokenKey: String = Pac4jConstants.CSRF_TOKEN
)

object SAMLAuthConfig {
  import pureconfig._
  import generic.auto._
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
        conf.saml.csrfTokenKey
      )
      with AutoInject
}
