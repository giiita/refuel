package refuel.oauth.authorize

import akka.http.scaladsl.model.Uri
import refuel.oauth.GrantScope
import refuel.oauth.authorize.ResponseType.{Code, Token}

/** Authorize request
  *
  * Since OAuth 2.1, PKCE verification is mandatory.
  *
  * @param responseType (Required) Response type.
  *                     Must be either code or token. Alternatively, the response type registered in the response type registory can be used.
  * @param clientId (Required) Authorized app client id.
  * @param scopes (Optional) Requested grant scopes.
  * @param state (Optional) An arbitrary string. A property to be received by the authorization endpoint and
  *              responded to during the authorization completion redirect to verify the hijacking on the client side.
  * @param redirectUri (optional) The redirect URI to which the authorization code will be sent.
  *                    Required if the uri is not registered in a pre-trusted application. Not required on request if there
  *                    is a single pre-registered URI, but must match exactly if specified. If there is more than one pre-registered
  *                    URI, it is required at the time of request and must exactly match one of the authenticated redirect_uri.
  */
sealed abstract class AuthorizeRequest(
    val responseType: ResponseType,
    val clientId: String,
    val scopes: Iterable[GrantScope],
    val state: Option[String],
    val redirectUri: Uri,
    val parameters: Map[String, String]
)

object AuthorizeRequest {

  def apply(
      responseType: ResponseType,
      clientId: String,
      scopes: Iterable[GrantScope],
      codeChallenge: Option[CodeChallenge] = None,
      state: Option[String],
      redirectUri: Uri,
      parameters: Map[String, String] = Map.empty
  ): AuthorizeRequest = responseType match {
    case Code  => AuthorizationCodeRequest(clientId, scopes, codeChallenge, state, redirectUri, parameters)
    case Token => ImplicitGrantRequest(clientId, scopes, state, redirectUri, parameters)
  }

  /** The authorization code grant receives an authorization request and
    * responds with an authorization code.
    *
    * The authorization request has several anti-hijacking flows, verifying
    * local state, registered redirect_uri, and storing the challenge code.
    *
    * @param clientId (Required) Authorized app client id.
    * @param scopes   (Optional) Requested grant scopes.
    * @param codeChallenge (optional) This is the challenge code that is checked during the access token request.
    *                     In the case of S256, send the hashed arbitrary string to the authorization endpoint and the pre-hashed arbitrary string to the token endpoint.
    * @param state    (Optional) An arbitrary string. A property to be received by the authorization endpoint and
    *                 responded to during the authorization completion redirect to verify the hijacking on the client side.
    * @param redirectUri (optional) The redirect URI to which the authorization code will be sent, which
    *                    can be specified in the request parameter or in advance when registering the authorization application. Either is required.
    */
  case class AuthorizationCodeRequest(
      override val clientId: String,
      override val scopes: Iterable[GrantScope],
      codeChallenge: Option[CodeChallenge],
      override val state: Option[String],
      override val redirectUri: Uri,
      override val parameters: Map[String, String] = Map.empty
  ) extends AuthorizeRequest(ResponseType.Code, clientId, scopes, state, redirectUri, parameters)

  /**
    * With implicit grant, there is no need to generate an authorization code.
    * After validating the authorization request, the redirect uri is sent with an access token.
    *
    * Since the client receives an access token as a result of the authorization request, no challenge is
    * performed, but the rest of the authorization verification flow is the same as for authorization code checking.
    *
    * @param clientId (Required) Authorized app client id.
    * @param scopes (Optional) Requested grant scopes.
    * @param state (Optional) An arbitrary string. A property to be received by the authorization endpoint and
    *              responded to during the authorization completion redirect to verify the hijacking on the client side.
    * @param redirectUri (optional) The redirect URI to which the authorization code will be sent, which
    *                    can be specified in the request parameter or in advance when registering the authorization application. Either is required.
    */
  @deprecated("Since OAuth2.1, this specification seems to disappear.")
  case class ImplicitGrantRequest(
      override val clientId: String,
      override val scopes: Iterable[GrantScope],
      override val state: Option[String],
      override val redirectUri: Uri,
      override val parameters: Map[String, String] = Map.empty
  ) extends AuthorizeRequest(ResponseType.Token, clientId, scopes, state, redirectUri, parameters)

}
