package refuel.oauth.authorize

import akka.http.scaladsl.model.Uri
import refuel.oauth.GrantScope

/** Authorize request
  *
  * responseType  (Required) Response type.
  *               Must be either code or token. Alternatively, the response type registered in the response type registory can be used.
  * clientId      (Required) Authorized app client id.
  * scopes        (Optional) Requested grant scopes.
  * codeChallenge (optional) This is the challenge code that is checked during the access token request.
  *               In the case of S256, send the hashed arbitrary string to the authorization endpoint and the pre-hashed arbitrary string to the token endpoint.
  * state         (Optional) An arbitrary string. A property to be received by the authorization endpoint and
  *               responded to during the authorization completion redirect to verify the hijacking on the client side.
  * redirectUri   (optional) The redirect URI to which the authorization code will be sent, which must be exactly the same as the one
  *               specified at the time of the authorization request, regardless of whether it is directly consistent with the redirected
  *               destination of the authenticated Application.
  *               Therefore, it must be possible to fully recover the redirect_uri from the authorization code issued at the time of the authorization request.
  *
  * Translated with www.DeepL.com/Translator (free version)
  */
trait AuthorizeState[+U] extends AuthProfile[U] {
  val ownerProfile: U
  val clientId: String
  val scopes: Iterable[GrantScope]
  val codeChallenge: Option[CodeChallenge]
  val redirectUri: Option[Uri]
}
