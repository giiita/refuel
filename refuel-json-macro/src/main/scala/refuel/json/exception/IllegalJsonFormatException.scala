package refuel.json.exception

case class IllegalJsonFormatException(msg: String, e: Throwable = null) extends RuntimeException(msg, e) {}
