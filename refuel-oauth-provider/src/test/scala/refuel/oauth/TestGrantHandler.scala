package refuel.oauth

import refuel.oauth.authorize.{AuthProfile, AuthorizeState}
import refuel.oauth.client.AuthorizedApp
import refuel.oauth.grant.GrantHandler
import refuel.oauth.token.GrantRequest

import scala.concurrent.Future

class TestGrantHandler extends GrantHandler[Future, String] {

  /** Used all grant_types.
    *
    * Retrieve a persistent client to inspect for authorization requested clients.
    *
    * When Failed is returned, if the client-side redirect destination information is
    * known, it will be forwarded with an error (302), but if it is unknown, the endpoint
    * will respond with unauthorized (401).
    * In this case, the resource owner manipulating the application will experience a strange
    * screen transition, so the service provider should ensure that the redirect destination is specified.
    *
    * @param clientId Authorize app client id
    * @return Persistent client profile or failure.
    */
  override def findApp(clientId: String): Future[AuthorizedApp] = ???

  /** Validate the requested authorization scope.
    * If the authorization scope itself is invalid, return Failure. If subsequent
    * processing is to continue, create your own defined GrantScope.
    *
    * Depending on the specification, for example, if you receive an unsupported authorization
    * scope request, or if you are expecting one or more authorization scopes and there is no request,
    * you can notify the authorization redirection endpoint of the error by returning Failure as an invalid request.
    *
    * Require: Mixin AutoInject
    */
  override def verifyGrantScope(request: Iterable[String]): Future[Iterable[GrantScope]] = ???

  /** Used authorization_code grant.
    *
    * At the token endpoint, restores the authorization request from the authorization code issued by the authorization endpoint.
    *
    * @param authorizeCode Issued authorize token
    * @return Request state at the time of the authorization request. If the authorization has expired, FAILURE.
    */
  override def restoreAuthorization(authorizeCode: String): Future[AuthorizeState[String]] = ???

  /** Used authorization_code grant.
    *
    * At the token endpoint, restores the authorization request from the authorization code issued by the authorization endpoint.
    *
    * @param accessToken Issued authorize token
    * @return Request state at the time of the authorization request. If the authorization has expired, FAILURE.
    */
  override def verifyAccessToken(accessToken: String): Future[AuthProfile[String]] = ???

  /** Used all grant_types.
    *
    * Perform any tests to prove that it is a valid access token.
    * For example, you may need to verify the expiration date.
    *
    * If all grant_types are to be supported, these verifications need to be implemented.
    *
    * - [[GrantRequest.PasswordGrantRequest]]
    * - [[GrantRequest.ClientCredentialsGrantRequest]]
    * - [[GrantRequest.AuthorizeCodeGrantRequest]]
    * - [[GrantRequest.RefreshTokenGrantRequest]]
    *
    * @return
    */
  override def verifyCredentials: PartialFunction[GrantRequest, Future[AuthProfile[String]]] = ???
}
