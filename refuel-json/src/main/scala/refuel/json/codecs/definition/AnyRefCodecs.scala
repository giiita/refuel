package refuel.json.codecs.definition

import refuel.json.JsonVal
import refuel.json.codecs.{CodecRaiseable, CodecTyper}
import refuel.json.entry._
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializeType, UnsupportedOperation}

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
  private[this] def IterableOnceCodec[T, C[_] <: Iterable[_], D[_] <: CodecRaiseable[_]](
      c: => C[T],
      j: (C[T], T) => C[T]
  )(tct: D[T])(implicit mapper: CodecTyper[D]): D[C[T]] = {
    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into ${this.getClass.getName}", e)
    }

    mapper.build[C[T]](
      {
        case JsArray(x) =>
          x.foldLeft(c: C[T]) {
            case (a, b) => j(a, mapper.read(b)(tct))
          }
        case JsNull => c
        case bf     => throw fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
      },
      { x: C[T] => JsArray(x.asInstanceOf[Iterable[T]].map(q => mapper.write[T](q)(tct))) }
    )
  }

  /**
    * [[Seq]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SeqCodec[T, C[_] <: CodecRaiseable[_]: CodecTyper](_x: C[T]): C[Seq[T]] =
    IterableOnceCodec[T, Seq, C](Nil, _ :+ _)(_x)

  /**
    * [[Set]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SetCodec[T, C[_] <: CodecRaiseable[_]: CodecTyper](_x: C[T]): C[Set[T]] =
    IterableOnceCodec[T, Set, C](Set.empty, _ + _)(_x)

  /**
    * [[Vector]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def VectorCodec[T, C[_] <: CodecRaiseable[_]: CodecTyper](_x: C[T]): C[Vector[T]] =
    IterableOnceCodec[T, Vector, C](Vector.empty, _ :+ _)(_x)

  /**
    * [[Array]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ArrayCodec[T: ClassTag, C[_] <: CodecRaiseable[_]](
      _x: C[T]
  )(implicit mapper: CodecTyper[C]): C[Array[T]] = {
    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into ${this.getClass.getName}", e)
    }

    mapper.build[Array[T]](
      {
        case JsArray(x) =>
          x.foldLeft[Array[T]](Array.empty) {
            case (a, b) => a.:+(mapper.read(b)(_x))
          }
        case JsNull => Array.empty[T]
        case bf     => throw fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
      },
      t => JsArray(t.map(mapper.write(_)(_x)))
    )
  }

  /**
    * [[List]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ListCodec[T, C[_] <: CodecRaiseable[_]: CodecTyper](_x: C[T]): C[List[T]] =
    IterableOnceCodec[T, List, C](List.empty, _ :+ _)(_x)

  /**
    * [[Map]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  implicit final def MapCodec[K, V, C[_] <: CodecRaiseable[_]](
      _x: (C[K], C[V])
  )(implicit mapper: CodecTyper[C]): C[Map[K, V]] = {
    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into ${this.getClass.getName}", e)
    }

    mapper.build[Map[K, V]](
      {
        case JsObject(x) =>
          Map(
            x.map {
              case (k, v) => mapper.read(k)(_x._1) -> mapper.read(v)(_x._2)
            }: _*
          )
        case JsNull => Map()
        case bf     => throw fail(bf, UnsupportedOperation("Only JsArray or JsObject can be Seq[T] decoded"))
      },
      x =>
        JsObject(
          x.map {
            case (k, v) => mapper.write(k)(_x._1).asInstanceOf[JsString] -> mapper.write(v)(_x._2)
          }.toSeq
        )
    )
  }

  /**
    * [[Option]] codec generator.
    *
    * @param _x Codec of option param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def OptionCodec[T, C[_] <: CodecRaiseable[_]](
      _x: C[T]
  )(implicit mapper: CodecTyper[C]): C[Option[T]] = {
    mapper.build[Option[T]](
      {
        case JsNull => None
        case bf =>
          Try {
            mapper.read(bf)(_x)
          }.toOption
      },
      x => x.fold(JsEmpty: JsonVal)(t => mapper.write(t)(_x))
    )
  }
}
