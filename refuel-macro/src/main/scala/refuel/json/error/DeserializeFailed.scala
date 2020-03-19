package refuel.json.error

abstract class DeserializeFailed(msg: String, th: Throwable) extends RuntimeException(msg, th)

final case class DeserializeFailPropagation(msg: String, th: Throwable) extends DeserializeFailed(msg, th)

final case class UnexpectedDeserializeType(msg: String, th: Throwable) extends DeserializeFailed(msg, th)

final case class UnexpectedDeserializedCollectionSize(msg: String) extends DeserializeFailed(msg, null)

final case class CannotAccessJsonKey(msg: String) extends DeserializeFailed(msg, null)

final case class CodecBuildException(msg: String) extends Exception(msg)

final case class UnsupportedOperation(msg: String ) extends DeserializeFailed(msg, null)
