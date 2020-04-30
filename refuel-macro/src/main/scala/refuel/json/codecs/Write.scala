package refuel.json.codecs

import refuel.json.{Codec, JsonVal, WriteOnly}

/**
  * This is a writer of json syntax tree.
  *
  * @tparam T Writable json type by this codec.
  */
trait Write[T] extends CodecRaiseable[T] { me =>

  /**
    * Serialize Json to T format.
    * Failure should continue and propagate up.
    *
    * @param t Serializable object.
    * @return
    */
  def serialize(t: T): JsonVal

  override def raise: Codec[T] = new Codec[T] with WriteOnly[T] {
    override final def serialize(t: T): JsonVal = me.serialize(t)
  }
}
