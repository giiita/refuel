package refuel.oauth.exception

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query

case class AuthorizeChallengeFailed(
    uri: Uri,
    state: Option[String],
    error: String,
    description: Option[String],
    cause: Throwable = null
) extends AuthorizeChallengeException(
      s"Failure redirection to ${uri.withQuery(Query(Map("error" -> error) ++ description.map("error_description".->)))}",
      cause
    ) {
  def toQueryMap: Map[String, String] =
    Map(
      "error" -> error
    ) ++ state.map("state".->) ++ description.map("error_description".->)
}

object AuthorizeChallengeFailed {
  def build(
      error: String,
      state: Option[String],
      cause: Throwable,
      uri: Option[Uri] = None
  ): AuthorizeChallengeException =
    uri.fold[AuthorizeChallengeException](
      new AuthorizeRejectionException(s"$error: ${cause.getMessage}", cause)
    )(x => new AuthorizeChallengeFailed(x, state, error, Some(cause.getMessage), cause))
}
