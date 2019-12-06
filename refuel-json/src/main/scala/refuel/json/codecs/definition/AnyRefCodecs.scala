package refuel.json.codecs.definition

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.builder.context.keylit.SelfCirculationLit
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializeType, UnsupportedOperation}
import refuel.json.entry._
import refuel.json.{Codec, Json}

import scala.language.implicitConversions
import scala.reflect.ClassTag

private[codecs] trait AnyRefCodecs {

  /**
    * Codec base class for classes that inherit iterable.
    *
    * @param c    Empty value.
    * @param j    Iterable append function.
    * @param tct  Inner type parameter codec symbol.
    *
    * @tparam T Inner type parameter.
    * @tparam C Collection types.
    */
  private[this] class IterableOnceCodec[T, C[T] <: Iterable[T]]
  (c: => C[T], j: (C[T], T) => C[T])(tct: Codec[T]) extends Codec[C[T]] {

    def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to ${this.getClass.getName} -> $bf", e)
    }

    /**
      * Deserialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param bf Json syntax tree.
      * @return
      */
    override def deserialize(bf: Json): Either[DeserializeFailed, C[T]] = {
      bf match {
        case JsArray(x)  =>
          x.foldLeft(Right(c): Either[DeserializeFailed, C[T]]) { (a, b) =>
            a.right.flatMap(list => b.to[T](this.tct).right.map(j(list, _)))
          }
        case JsObject(x) =>
          x.foldLeft(Right(c): Either[DeserializeFailed, C[T]]) { (a, b) =>
            a.right.flatMap(list => JsEntry(b._1, b._2).to[T](this.tct).right.map(j(list, _)))
          }
        case JsNull      => Right(c)
        case _           => Left(
          fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
        )
      }
    }

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def serialize(t: C[T]): Json = {
      JsArray(t.map(this.tct.serialize))
    }

    override val keyLiteralRef: JsKeyLitOps = SelfCirculationLit
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
  implicit final def VectorCodec[T](_x: Codec[T]): Codec[Vector[T]] = new IterableOnceCodec[T, Vector](Vector.empty, _ :+ _)(_x)

  /**
    * [[Array]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ArrayCodec[T: ClassTag](_x: Codec[T]): Codec[Array[T]] = new Codec[Array[T]] {
    def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to ${this.getClass.getName} -> $bf", e)
    }

    /**
      * Deserialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param bf Json syntax tree.
      * @return
      */
    override def deserialize(bf: Json): Either[DeserializeFailed, Array[T]] = {
      bf match {
        case JsArray(x)  =>
          x.foldLeft(Right(Array.empty): Either[DeserializeFailed, Array[T]]) { (a, b) =>
            a.right.flatMap(list => b.to[T](_x).right.map(list.:+))
          }
        case JsObject(x) =>
          x.foldLeft(Right(Array.empty): Either[DeserializeFailed, Array[T]]) { (a, b) =>
            a.right.flatMap(list => JsEntry(b._1, b._2).to[T](_x).right.map(list.:+))
          }
        case JsNull      => Right(Array.empty)
        case _           => Left(
          fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
        )
      }
    }

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def serialize(t: Array[T]): Json = {
      JsArray(t.map(_x.serialize))
    }

    override val keyLiteralRef: JsKeyLitOps = SelfCirculationLit
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

    def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Map -> $bf", e)
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Map[K, V]] = {
      bf match {
        case JsEntry(k, v) =>
          for {
            kr <- _x._1.deserialize(k).right
            vr <- _x._2.deserialize(v).right
          } yield Map(kr -> vr)
        case JsObject(x)   =>
          x.foldRight(Right(Map()): Either[DeserializeFailed, Map[K, V]]) { (a, b) =>
            b.right.flatMap { x =>
              for {
                kr <- _x._1.deserialize(a._1).right
                vr <- _x._2.deserialize(a._2).right
              } yield x.+(kr -> vr)
            }
          }
        case JsNull        => Right(Map())
        case _             => Left(
          fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
        )
      }
    }

    override def serialize(t: Map[K, V]): Json = {
      JsObject(
        t.toIndexedSeq.map {
          case (k, v) => _x._1.serialize(k).asInstanceOf[JsLiteral] -> _x._2.serialize(v)
        }
      )
    }

    override val keyLiteralRef: JsKeyLitOps = SelfCirculationLit
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

      def fail(bf: Json, e: Throwable): DeserializeFailed = {
        UnexpectedDeserializeType(s"Cannot deserialize to Option -> $bf", e)
      }

      override def deserialize(bf: Json): Either[DeserializeFailed, Option[T]] = {
        bf match {
          case JsNull => Right(None)
          case _      => _x.deserialize(bf).right.map(Some.apply[T])
        }
      }

      override def serialize(t: Option[T]): Json = {
        t.fold(JsEmpty: Json) { x =>
          _x.serialize(x)
        }
      }

      override val keyLiteralRef: JsKeyLitOps = _x.keyLiteralRef
    }
  }
}
