package refuel.json.compiletime

import refuel.json.JsonVal
import refuel.json.`macro`.TypedCodecMacro
import refuel.json.codecs.{Codec, Read}

import scala.compiletime.{erasedValue, error, summonFrom}
import scala.deriving.Mirror

private[refuel] object DeriveReads extends ProductProjection {

  inline def inferRead[A]: Read[A] = {
    summonFrom[Read[A]] {
      case x: Read[A] => x
      case _: Mirror.ProductOf[A] => DeriveReads.derivedRead[A]
      case _ => error("It is not possible to derive a class that does not support Product. Use Derive[T] instead, or define an implicit scope for the Codec of a non-Product class that depends on it internally.")
    }
  }

  transparent inline def derivedRead[A](using A: Mirror.ProductOf[A]): Read[A] =
    new Read[A] {
      private[this] val elemLabels = inferLabels[A.MirroredElemLabels]

      private[this] val elemReads: List[Read[_]] =
        inferReads[A.MirroredElemTypes]

      private[this] val elemSignature = elemLabels.zip(elemReads).zipWithIndex

      private[this] val elemCount = elemSignature.size

      override def deserialize(bf: JsonVal): A = {
        val buffer = new Array[Any](elemCount)
        elemSignature.foreach { case ((label, read), i) =>
          buffer(i) = {
            read.deserialize(bf.named(label))
          }
        }
        A.fromProduct(
          new Product{
            override def canEqual(that: Any): Boolean = true
            override def productArity: Int = elemCount
            override def productElement(n: Int): Any =
              buffer(n)
          }
        )
      }
    }

  private inline def inferReads[T <: Tuple]: List[Read[_]] = foldReads[T]

  private inline def foldReads[T <: Tuple]: List[Read[_]] =
    inline erasedValue[T] match {
      case _: EmptyTuple =>
        Nil
      case _: (t *: ts) =>
        inferRead[t] :: foldReads[ts]
    }
}
