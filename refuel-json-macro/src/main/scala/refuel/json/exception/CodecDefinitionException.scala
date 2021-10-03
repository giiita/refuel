package refuel.json.exception

case class CodecDefinitionException(msg: String, e: Throwable = null) extends RuntimeException(msg, e)
