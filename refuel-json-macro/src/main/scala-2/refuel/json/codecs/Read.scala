package refuel.json.codecs

import refuel.json.JsonVal

import scala.util.Try

/**
  * This is a reader of json syntax tree.
  *
  * @tparam T Readable json type by this codec.
  */
trait Read[T] extends CodecContextType { me =>
  
  /**
    * Deserialize Json to T format.
    * Failure should continue and propagate up.
    *
    * @param bf Json syntax tree.
    * @return
    */
  def deserialize(bf: JsonVal): T
}

object Read {
  private[refuel] def apply[T](construct: JsonVal => Try[T]): Read[T] = new Read[T] {
    def deserialize(bf: JsonVal): T = construct(bf).fold[T](throw _, x => x)
  }
}