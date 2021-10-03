package refuel.json.compiletime

import refuel.json.JsonVal.JsObject
import refuel.json.`macro`.TypedCodecMacro
import refuel.json.codecs.{Codec, Write}
import refuel.json.{Json, JsonVal}

import scala.compiletime.{erasedValue, error, summonFrom}
import scala.deriving.Mirror

private[refuel] object DeriveWrites extends ProductProjection {

  inline def inferWrite[A]: Write[A] = {
    summonFrom[Write[A]] {
      case x: Write[A] => x
      case _: Mirror.ProductOf[A] => DeriveWrites.derivedWrite[A]
      case _ => error("It is not possible to derive a class that does not support Product. Use Derive[T] instead, or define an implicit scope for the Codec of a non-Product class that depends on it internally.")
    }
  }

  inline def derivedWrite[A](using inline A: Mirror.ProductOf[A]): Write[A] =
    new Write[A] {
      private[this] val elemLabels = inferLabels[A.MirroredElemLabels]

      private[this] val elemDecoders: List[Write[_]] =
        inferWrites[A.MirroredElemTypes]

      private[this] val elemSignature = elemLabels.zip(elemDecoders).zipWithIndex

      private[this] val elemCount = elemSignature.size

      override def serialize(t: A): JsonVal = {
        val entries = t.asInstanceOf[Product].productIterator.toArray
        JsObject.pure(
          (0 until elemCount).map { i =>
            Json.str(elemLabels(i)) -> elemDecoders(i).asInstanceOf[Write[Any]].serialize(entries(i))
          }
        )
      }
    }

  private inline def inferWrites[T <: Tuple]: List[Write[_]] = foldWrites[T]

  private inline def foldWrites[T <: Tuple]: List[Write[_]] =
    inline erasedValue[T] match {
      case _: EmptyTuple =>
        Nil
      case _: (t *: ts) =>
        inferWrite[t] :: foldWrites[ts]
    }
}
