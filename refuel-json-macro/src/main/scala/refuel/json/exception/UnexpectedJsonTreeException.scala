package refuel.json.exception

case class UnexpectedJsonTreeException(msg: String) extends RuntimeException(msg) {}
