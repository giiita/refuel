package refuel.json.codecs

import refuel.json.JsonVal

import scala.util.Try

/**
  * This is a writer of json syntax tree.
  *
  * @tparam T Writable json type by this codec.
  */
trait Write[T] extends CodecContextType { me =>
  /**
    * Serialize Json to T format.
    * Failure should continue and propagate up.
    *
    * for {
    *   str <- Write[Str]
    *
    * @param t Serializable object.
    * @return
    */
  def serialize(t: T): JsonVal

  trait X extends Product
}

object Write {
  private[refuel] def apply[T](destruct: T => Try[JsonVal]): Write[T] = new Write[T] {
    def serialize(t: T): JsonVal = destruct(t).fold(throw _, x => x)
  }
}

