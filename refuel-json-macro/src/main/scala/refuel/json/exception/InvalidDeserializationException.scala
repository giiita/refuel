package refuel.json.exception

case class InvalidDeserializationException(msg: String, e: Throwable = null) extends RuntimeException(msg, e)
