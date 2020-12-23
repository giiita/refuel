package refuel.oauth.authorize

import refuel.oauth.GrantScope

trait AuthProfile[+U] {
  val ownerProfile: U
  val clientId: String
  val scopes: Iterable[GrantScope]
}
