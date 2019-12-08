package refuel.json

import refuel.json.codecs.{Read, Write}
import refuel.json.error.DeserializeFailed

import scala.annotation.implicitNotFound

/**
  * Apply / Unapply codec of T
  *
  * @tparam T Target type param.
  */
@implicitNotFound("Cannot found ${T}")
trait Codec[T] extends Read[T] with Write[T] {

}
