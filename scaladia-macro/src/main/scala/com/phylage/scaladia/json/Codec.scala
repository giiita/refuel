package com.phylage.scaladia.json

import com.phylage.scaladia.json.codecs.{Read, Write}
import com.phylage.scaladia.json.error.DeserializeFailed

import scala.annotation.implicitNotFound


/**
  * Apply / Unapply codec of T
  *
  * @tparam T Target type param.
  */
@implicitNotFound("Cannot found ${T}")
trait Codec[T] extends Read[T] with Write[T] {
}

object Codec {
  /**
    * One of the function to generate codec.
    *
    * @param r Reader symbol.
    * @param w Writer symbol.
    * @tparam T Codec type.
    * @return
    */
  def apply[T](r: Read[T], w: Write[T]): Codec[T] = new Codec[T] {
    /**
      * Deserialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param bf Json syntax tree.
      * @return
      */
    def deserialize(bf: Json): Either[DeserializeFailed, T] = r.deserialize(bf)

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    def serialize(t: T): Json = w.serialize(t)
  }
}
