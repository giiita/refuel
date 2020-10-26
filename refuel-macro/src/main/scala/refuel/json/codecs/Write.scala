package refuel.json.codecs

import refuel.json.{Codec, JsonVal, WriteOnly}

/**
  * This is a writer of json syntax tree.
  *
  * @tparam T Writable json type by this codec.
  */
trait Write[T] extends CodecRaiseable[T] { me =>

  /** Write union codec.
    *
    * {{{
    * (
    *   WriteWith("id")(CaseClassCodec.from[Id]) |+| WriteWith("name")(CaseClassCodec.from[Name])
    * ).serialize(Id("foo") -> Name("bar")) == Json.obj(
    *   "id" -> "foo",
    *   "name" -> "bar"
    * )
    * }}}
    *
    * @param that Other writes.
    * @tparam A Union type
    * @return
    */
  def |+|[A](that: Write[A]): Write[(T, A)] = new Write[(T, A)] {

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def serialize(t: (T, A)): JsonVal = me.serialize(t._1) ++ that.serialize(t._2)
  }

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
