package refuel.json.codecs

import refuel.json.JsonVal

import scala.annotation.implicitNotFound
import scala.language.implicitConversions
import scala.util.Try

/**
  * Apply / Unapply codec of T
  *
  * @tparam T Target type param.
  */
@implicitNotFound("No found codec of type ${T}")
trait Codec[T] extends Read[T] with Write[T] {
}

object Codec {
  def apply[T](const: JsonVal => Try[T], dest: T => Try[JsonVal]): Codec[T] = new Codec[T] {
    override def deserialize(bf: JsonVal): T = const(bf).fold[T](throw _, x => x)
    override def serialize(t: T): JsonVal = dest(t).fold(throw _, x => x)
  }
}

