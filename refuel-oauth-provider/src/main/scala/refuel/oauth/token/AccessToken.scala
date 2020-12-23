package refuel.oauth.token

import java.time.ZonedDateTime

import refuel.oauth.GrantScope

/**
  * Access token
  *
  * @param token Access token is used to authentication.
  * @param refreshToken Refresh token is used to re-issue access token.
  * @param scope Inform the client of the scope of the access token issued.
  * @param lifeSeconds Life of the access token since its creation. In seconds.
  * @param issuedAt Access token is created date.
  * @param params Additional parameters to add information/restriction on given Access token.
  */
case class AccessToken(
    token: String,
    refreshToken: Option[String],
    scope: Iterable[GrantScope],
    lifeSeconds: Option[Long],
    issuedAt: ZonedDateTime,
    params: Map[String, String] = Map.empty[String, String]
) {
  val expiresIn: Option[Long] = expirationTime map { _ - System.currentTimeMillis / 1000 }

  def isExpired: Boolean = expiresIn.exists(_ < 0)

  private[this] def expirationTime: Option[Long] = lifeSeconds map { lifeSecs => issuedAt.toEpochSecond + lifeSecs }
}
