package refuel.oauth.client

import akka.http.scaladsl.model.Uri
import refuel.oauth.exception.{AuthorizeChallengeException, InvalidGrantException}

import scala.util.{Failure, Try}

/** Authorized application profile.
  * First, register the authorized client.
  *
  */
trait AuthorizedApp {
  val clientId: String
  val clientSecret: String
  val callbackUri: Iterable[Uri]

  /** If more than one redirect URI is registered, if only a portion of the redirect URI is
    * registered, or if no redirect URI is registered, the client must include the redirect URI
    * in the authorization request using the redirect_uri request parameter.
    *
    * If the authorization request contains a redirect URI, and the redirect URI has been
    * pre-registered, the authorization server must compare the value contained in the
    * authorizationrequest with the registered redirect URI (or URI component) and verify that
    * it matches at least one of them, as shown in [RFC3986]. If the client's registration
    * contains a full redirect URI, the server must check that it matches. If the client's registration
    * contains a full redirect URI, the authorization server MUST compare the two URIs
    * using a simple string comparison as defined in [RFC3986].
    *
    * @param requested Requested URI
    * @return
    */
  def verifyRedirection(requested: Option[Uri]): Try[Uri] = {
    if ((callbackUri ++ requested).exists(_.fragment.nonEmpty)) {
      Failure(new AuthorizeChallengeException("Fragment components cannot be included in redirects."))
    } else {
      {
        if (callbackUri.isEmpty) {
          requested
        } else {
          callbackUri match {
            case x if x.size == 1 && requested.isEmpty => Some(x.head)
            case _                                     => requested.flatMap(x => callbackUri.find(_ == x))
          }
        }
      }.toRight(new AuthorizeChallengeException("Invalid redirection setting.")).toTry.map(_.withoutFragment)
    }
  }
}
