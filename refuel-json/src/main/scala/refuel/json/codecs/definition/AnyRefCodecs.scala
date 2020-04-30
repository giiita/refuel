package refuel.json.codecs.definition

import refuel.json.entry._
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializeType, UnsupportedOperation}
import refuel.json.{Codec, JsonVal}

import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.util.Try

private[codecs] trait AnyRefCodecs {

  /**
    * Codec base class for classes that inherit iterable.
    *
    * @param c   Empty value.
    * @param j   Iterable append function.
    * @param tct Inner type parameter codec symbol.
    * @tparam T Inner type parameter.
    * @tparam C Collection types.
    */
  private[this] class IterableOnceCodec[T, C[T] <: Iterable[T]](c: => C[T], j: (C[T], T) => C[T])(tct: Codec[T])
      extends Codec[C[T]] {

    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to ${this.getClass.getName} -> $bf", e)
    }

    /**
      * Deserialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param bf Json syntax tree.
      * @return
      */
    override def deserialize(bf: JsonVal): C[T] = {
      bf match {
        case JsArray(x) =>
          x.foldLeft(c: C[T]) {
            case (a, b) => j(a, b.to(this.tct))
          }
        case JsNull => c
        case _      => throw fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
      }
    }

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def serialize(t: C[T]): JsonVal = {
      JsArray(t.map(this.tct.serialize))
    }
  }

  /**
    * [[Seq]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SeqCodec[T](_x: Codec[T]): Codec[Seq[T]] = new IterableOnceCodec[T, Seq](Nil, _ :+ _)(_x)

  /**
    * [[Set]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SetCodec[T](_x: Codec[T]): Codec[Set[T]] = new IterableOnceCodec[T, Set](Set.empty, _ + _)(_x)

  /**
    * [[Vector]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def VectorCodec[T](_x: Codec[T]): Codec[Vector[T]] =
    new IterableOnceCodec[T, Vector](Vector.empty, _ :+ _)(_x)

  /**
    * [[Array]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ArrayCodec[T: ClassTag](_x: Codec[T]): Codec[Array[T]] = new Codec[Array[T]] {
    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to ${this.getClass.getName} -> $bf", e)
    }

    /**
      * Deserialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param bf Json syntax tree.
      * @return
      */
    override def deserialize(bf: JsonVal): Array[T] = {
      bf match {
        case JsArray(x) =>
          x.foldLeft[Array[T]](Array.empty) {
            case (a, b) => a.:+(b.to(_x))
          }
        case JsNull => Array.empty[T]
        case _      => throw fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
      }
    }

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def serialize(t: Array[T]): JsonVal = {
      JsArray(t.map(_x.serialize))
    }
  }

  /**
    * [[List]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ListCodec[T](_x: Codec[T]): Codec[List[T]] = new IterableOnceCodec[T, List](List.empty, _ :+ _)(_x)

  /**
    * [[Map]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  implicit final def MapCodec[K, V](_x: (Codec[K], Codec[V])): Codec[Map[K, V]] = new Codec[Map[K, V]] {

    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Map -> $bf", e)
    }

    override def deserialize(bf: JsonVal): Map[K, V] = {
      bf match {
        case JsObject(x) =>
          Map(
            x.map {
              case (k, v) => _x._1.deserialize(k) -> _x._2.deserialize(v)
            }: _*
          )
        case JsNull => Map()
        case _      => throw fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
      }
    }

    override def serialize(t: Map[K, V]): JsonVal = {
      JsObject(
        t.map {
          case (k, v) => _x._1.serialize(k).asInstanceOf[JsString] -> _x._2.serialize(v)
        }.toSeq
      )
    }
  }

  /**
    * [[Option]] codec generator.
    *
    * @param _x Codec of option param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def OptionCodec[T](_x: Codec[T]): Codec[Option[T]] = {
    new Codec[Option[T]] {

      def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
        UnexpectedDeserializeType(s"Cannot deserialize to Option -> $bf", e)
      }

      override def deserialize(bf: JsonVal): Option[T] = {
        bf match {
          case JsNull => None
          case _ =>
            Try {
              _x.deserialize(bf)
            }.toOption
        }
      }

      override def serialize(t: Option[T]): JsonVal = {
        t.fold(JsEmpty: JsonVal) { x => _x.serialize(x) }
      }
    }
  }
}
