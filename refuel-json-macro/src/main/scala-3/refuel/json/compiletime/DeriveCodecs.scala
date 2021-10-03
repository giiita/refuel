package refuel.json.compiletime

import refuel.json.JsonVal
import refuel.json.codecs.Codec

import scala.compiletime.{erasedValue, error, summonFrom}
import scala.deriving.Mirror

private[refuel] object DeriveCodecs extends refuel.inject.Injector {
  inline def inferCodec[A]: Codec[A] = {
    summonFrom[Codec[A]] {
      case given Codec[A] => implicitly[Codec[A]]
      case _: Mirror.ProductOf[A] => DeriveCodecs.derivedCodec[A]
      case _ => error("It is not possible to derive a class that does not support Product. Use Derive[T] instead, or define an implicit scope for the Codec of a non-Product class that depends on it internally.")
    }
  }

  inline def derivedCodec[A](using inline A: Mirror.ProductOf[A]): Codec[A] =
    new Codec[A] {
      override def serialize(t: A): JsonVal = DeriveWrites.inferWrite[A].serialize(t)

      override def deserialize(bf: JsonVal): A = DeriveReads.inferRead[A].deserialize(bf)
    }

  inline def deriveCodecWithIndex[A](using inline A: Mirror.ProductOf[A]): Codec[A] =
    new Codec[A] {
      override def serialize(t: A): JsonVal = DeriveWritesWithIndex.inferWrite[A].serialize(t)

      override def deserialize(bf: JsonVal): A = DeriveReadsWithIndex.inferRead[A].deserialize(bf)
    }
}
