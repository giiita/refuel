package refuel.oauth.exception

class AuthorizeRejectionException(msg: String, cause: Throwable = null) extends AuthorizeChallengeException(msg, cause)
