package refuel.oauth.exception

class UnauthorizedException(msg: String, cause: Throwable = null) extends RuntimeException(msg, cause) {}
