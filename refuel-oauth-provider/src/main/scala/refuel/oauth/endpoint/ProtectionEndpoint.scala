package refuel.oauth.endpoint

import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.injector.AutoInject
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.grant.GrantHandler

trait ProtectionEndpoint[F[_]] extends Directives with AutoInject {

  /** Apply a directive that validates the access token and extracts the authorized client and resource owner information.
    *
    * Careful: the resources you return after being oauth2 authenticated should almost always be limited to what
    *          can be seen by the permissions of the AuthProfile client and resource owner.
    *
    * @param accessToken access_token
    * @param grantHandler grant_handler
    * @tparam U Resource owner type
    * @return
    */
  def verify[U](accessToken: String, grantHandler: GrantHandler[F, U]): Directive1[AuthProfile[U]]
}
