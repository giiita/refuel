package refuel.saml

import java.security.Security

import akka.actor.ActorSystem
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.pac4j.core.config.Config
import org.pac4j.saml.client.SAML2Client
import org.pac4j.saml.config.SAML2Configuration
import refuel.AkkaHttpSecurity
import refuel.injector.AutoInject
import refuel.session.SessionIDGenerator
import refuel.store.SessionStorage

/** {{{
  * class Controller(securityBuilder: AuthnSecrityBuilder) {
  *   securityBuilder.build(samlConfig, )
  * }
  * }}}
  *
  * @param conf injected
  * @param gen injected
  * @param storage injected
  */
class AuthnSAMLBuilder(conf: SAMLAuthConfig, gen: SessionIDGenerator, storage: SessionStorage)(implicit as: ActorSystem)
    extends AutoInject {
  Security.addProvider(new BouncyCastleProvider())

  lazy final val DefaultSAMLConf: SAML2Configuration = {
    val config = new SAML2Configuration(
      conf.keystorePath,
      conf.keystorePassword,
      conf.privateKeyPassword,
      conf.idpMetadataPath
    )
    conf.spMetadataPath.foreach(config.setServiceProviderMetadataPath)
    config.setAuthnRequestBindingType(conf.authnRequestBindingType)
    config
  }

  lazy final val DefaultSAMLClient: SAML2Client = {
    val client = new SAML2Client(DefaultSAMLConf)
    client.setCallbackUrl(conf.callbackurl)
    client
  }

  lazy final val Default: AkkaHttpSecurity = {
    build(
      new Config(DefaultSAMLClient)
    )
  }

  def build(config: Config)(implicit as: ActorSystem) =
    new AkkaHttpSecurity(config, storage, conf, gen)
}
