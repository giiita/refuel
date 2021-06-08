package refuel.oauth.endpoint

import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.inject.AutoInject
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.grant.GrantHandler

trait TokenEndpoint[F[_]] extends Directives with AutoInject {

  /** Validates the authorization code and verifies that it is a proper authorization code.
    * Since this directive completes the verification flow defined in OAuth2.0, apply performs
    * the additional necessary verification and issues the access token.
    *
    * Since the algorithm for generating authorization codes and access tokens is not defined
    * in OAuth2.0, it is necessary to implement it independently, and in the process, it may be
    * necessary to persist stateful authorization codes etc...
    *
    * @param grantHandler
    * @tparam U Resource owner type
    * @return
    */
  def grant[U](grantHandler: GrantHandler[F, U]): Directive1[AuthProfile[U]]
}

object TokenEndpoint {
  object Constant {
    final lazy val GrantType    = "grant_type"
    final lazy val Code         = "code"
    final lazy val RedirectUri  = "redirect_uri"
    final lazy val CodeVerifier = "code_verifier"

    final lazy val RefreshToken = "refresh_token"

    final lazy val Username = "username"
    final lazy val Password = "password"
  }
}
