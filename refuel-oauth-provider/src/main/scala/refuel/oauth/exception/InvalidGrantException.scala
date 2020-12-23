package refuel.oauth.exception

class InvalidGrantException(msg: String, cause: Throwable = null) extends RuntimeException(msg, cause) {}
