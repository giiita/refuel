package refuel.oauth

import refuel.oauth.endpoint.{AuthorizeEndpoint, TokenEndpoint}
import refuel.oauth.grant.GrantHandler

import scala.concurrent.Future

trait OAuth2[U] extends OAuth2Provider[Future, U] {
  override val authorize: AuthorizeEndpoint[Future]
  override val token: TokenEndpoint[Future]
  override val grantHandler: GrantHandler[Future, U]
}
