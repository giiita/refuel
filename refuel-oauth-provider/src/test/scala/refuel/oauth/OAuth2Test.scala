package refuel.oauth

import refuel.oauth.endpoint.{AuthorizeEndpoint, TokenEndpoint}
import refuel.oauth.grant.GrantHandler

import scala.concurrent.Future

class OAuth2Test(
    override val authorize: AuthorizeEndpoint[Future],
    override val token: TokenEndpoint[Future],
    override val grantHandler: GrantHandler[Future, String]
) extends OAuth2[String] {}
