package refuel.json.compiletime

import refuel.json.JsonVal
import refuel.json.JsonVal.{JsArray, JsEntry, JsObject}
import refuel.json.codecs.Read
import refuel.json.exception.InvalidDeserializationException

import scala.compiletime.error
import scala.compiletime.{erasedValue, summonFrom}
import scala.deriving.Mirror

private[refuel] object DeriveReadsWithIndex extends ProductProjection {

  inline def inferRead[A]: Read[A] = {
    summonFrom[Read[A]] {
      case x: Read[A] => x
      case _: Mirror.ProductOf[A] => DeriveReads.derivedRead[A]
      case _ => error("It is not possible to derive a class that does not support Product. Use Derive[T] instead, or define an implicit scope for the Codec of a non-Product class that depends on it internally.")
    }
  }

  inline def derivedRead[A](using A: Mirror.ProductOf[A]): Read[A] =
    new Read[A] {
      private[this] val elemReads: List[Read[_]] =
        inferReads[A.MirroredElemTypes]

      private[this] val elemCount = elemReads.size

      override def deserialize(bf: JsonVal): A = {
        val buffer = new Array[Any](elemCount)
        bf match {
          case arr: JsArray =>
            elemReads.zipWithIndex.foreach { case (read, i) =>
              buffer(i) = {
                read.deserialize(arr.n(i))
              }
            }
          case obj: JsObject =>
            elemReads.zipWithIndex.foreach { case (read, i) =>
              buffer(i) = {
                read.deserialize(obj.named(s"_$i"))
              }
            }
          case ent: JsEntry =>
            elemReads.zipWithIndex.foreach { case (read, i) =>
              buffer(i) = {
                read.deserialize(ent.named(s"_$i"))
              }
            }
          case x =>
            throw InvalidDeserializationException(s"Can only deserialize JsonObject or JsonArray, but was ${x.getClass}")
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
