package refuel.json.codecs

import refuel.json.{Codec, JsonVal, ReadOnly}

/**
  * This is a reader of json syntax tree.
  *
  * @tparam T Readable json type by this codec.
  */
trait Read[T] extends CodecRaiseable[T] { me =>

  /**
    * Deserialize Json to T format.
    * Failure should continue and propagate up.
    *
    * @param bf Json syntax tree.
    * @return
    */
  def deserialize(bf: JsonVal): T

  override def raise: Codec[T] = new Codec[T] with ReadOnly[T] {
    override def deserialize(bf: JsonVal): T = me.deserialize(bf)
  }
}
