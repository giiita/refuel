package refuel.oauth

trait GrantScope {
  def id: String
}

object GrantScope {
  implicit class GrantScopes(scopes: Iterable[GrantScope]) {
    def serialize: String = scopes.map(_.id).mkString(" ")
  }
}
