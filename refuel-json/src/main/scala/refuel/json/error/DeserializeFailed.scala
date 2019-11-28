package refuel.json.error

final case class UnsupportedOperation(msg: String) extends Exception(msg)

final case class UnexpectedEitherKey[A](left: A, right: A, actual: A)
  extends DeserializeFailed(s"expect = $left or $right, actual = $actual", null)

final case class IllegalJsonFormat(msg: String) extends Exception(msg)
final case class IllegalJsonSyntaxTreeBuilding(msg: String) extends Exception(msg)
final case class StreamIndeterminate(msg: String) extends Exception(msg)
final case class NotStartExpectedSyntax(msg: String) extends Exception(msg)
final case class UnexpectedDeserializeOperation(msg: String) extends Exception(msg)

final case class TokenizeFailed(msg: String, rest: String)
