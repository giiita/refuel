package refuel.json.codecs

import refuel.json.{Codec, JsonVal}

/**
  * This is a reader of json syntax tree.
  *
  * @tparam T Readable json type by this codec.
  */
trait Read[T] { me =>

  /**
    * Deserialize Json to T format.
    * Failure should continue and propagate up.
    *
    * @param bf Json syntax tree.
    * @return
    */
  def deserialize(bf: JsonVal): T

  def |+|[A](that: Read[A]): Read[(T, A)] = new Read[(T, A)] {

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def deserialize(t: JsonVal): (T, A) = me.deserialize(t) -> that.deserialize(t)
  }
}

object Read {
//  implicit def __readRef[T](implicit _c: Codec[T]): Read[T] = _c
}
