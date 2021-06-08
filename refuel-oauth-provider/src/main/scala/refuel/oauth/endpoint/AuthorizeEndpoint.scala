package refuel.oauth.endpoint

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{Directive1, Directives}
import refuel.inject.AutoInject
import refuel.oauth.authorize.{AuthorizeCode, AuthorizeRequest}
import refuel.oauth.grant.GrantHandler
import refuel.oauth.token.AccessToken

trait AuthorizeEndpoint[F[_]] extends Directives with AutoInject {

  /** Authorize step 1
    *
    * Minimally inspect authorization requests.
    *
    * When an invalid authorization request is detected, if we know the redirect endpoint of
    * the service provider, we will redirect complete with an error code, but if we don't know
    * it, this endpoint will respond with an abnormal value such as 401.
    *
    * It is a directive that only checks the legitimacy of the authorization request. After this, it is
    * necessary to check any authentication and authorization information and redirect to the authorization screen
    * of the authorization server or to the redirect uri of the service provider, if necessary.
    *
    * Since the specific authentication and authorization algorithms from here on are not strictly
    * defined in OAuth2.0, an original implementation is required. However, it is possible to use `AAA` for
    * the redirect to the service provider after the authorization is completed.
    *
    */
  def validAuthorizeRequest(
      parameters: Map[String, String],
      grantHandler: GrantHandler[F, _]
  ): Directive1[AuthorizeRequest]

  /** Authorization step 2 (final)
    *
    * When the authorization is complete, the authorization request has been stored, and the authorization code
    * has been issued, the service provider must be notified of the authorization code.
    *
    * {{{
    *   path("authorize_endpoint") {
    *     validAuthorizeRequest { req =>
    *       grantAndIssueCode { code =>
    *         authorizeComplete(req, code)
    *       }
    *     }
    *   }
    * }}}
    *
    * @param request Valid authorization request
    * @param authorizeCode Issued authorize code.
    * @return
    */
  def authorizeComplete(request: AuthorizeRequest, authorizeCode: AuthorizeCode): ToResponseMarshallable

  /** Complete the authorization request.
    * Redirect to the authorization endpoint with an access token, following the conventions.
    *
    * @param request Valid authorization request
    * @param accessToken Issued access token
    * @return
    */
  @deprecated("Since OAuth2.1, this specification seems to disappear.")
  def implicitGrantComplete(request: AuthorizeRequest, accessToken: AccessToken): ToResponseMarshallable
}
