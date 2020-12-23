package refuel.oauth

import akka.http.scaladsl.model.Uri.Query
import refuel.json.codecs.Write
import refuel.json.{CodecDef, Json}

package object token extends CodecDef {
  private[refuel] implicit lazy final val AccessTokenCodec: Write[AccessToken] = Serialize { token =>
    Json.obj(
      "token_type"    -> "Bearer",
      "token"         -> token.token,
      "refresh_token" -> token.refreshToken,
      "scope"         -> token.scope.serialize,
      "expire_in"     -> token.expiresIn
    )
  }

  // Use from implicit grant.
  private[refuel] implicit final def TokenToQuery(accessToken: AccessToken): Query =
    Query(accessToken.copy(refreshToken = None).ser.des[Map[String, String]])
}
