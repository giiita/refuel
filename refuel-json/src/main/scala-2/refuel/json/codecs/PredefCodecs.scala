package refuel.json.codecs

import refuel.json.JsonVal.{JsAny, JsArray, JsEmpty, JsNull, JsObject, JsString}
import refuel.json.codecs.datetime.DateTimeConversionStrategy
import refuel.json.exception.{CodecDefinitionException, IllegalJsonFormatException, InvalidDeserializationException}
import refuel.json.{Json, JsonTransform, JsonVal}
import refuel.lang.RuntimeTZ

import java.time.ZonedDateTime
import scala.jdk.CollectionConverters.MapHasAsScala
import scala.reflect.ClassTag
import scala.util.Try

trait PredefCodecs { _: JsonTransform =>

  implicit final def JsonValCodec: Codec[JsonVal] = new Codec[JsonVal] {
    override def serialize(t: JsonVal): JsonVal    = t
    override def deserialize(bf: JsonVal): JsonVal = bf
  }
  implicit final def UnitCodec: Codec[Unit] = new Codec[Unit] {
    override def serialize(t: Unit): JsonVal    = JsEmpty
    override def deserialize(bf: JsonVal): Unit = ()
  }

  implicit final def ZonedDateTimeCodec(
      implicit rt: RuntimeTZ,
      strategy: DateTimeConversionStrategy = DateTimeConversionStrategy.Formatter
  ): Codec[ZonedDateTime] =
    new Codec[ZonedDateTime] {
      override def serialize(t: ZonedDateTime): JsonVal = strategy.serialize(t).fold(throw _, x => x)

      override def deserialize(bf: JsonVal): ZonedDateTime = {
        strategy.deserialize(bf).fold(throw _, x => x)
      }
    }

  implicit final def TryCodec[T, C[_]](codec: C[T])(implicit projector: CodecTypeProjection[C]): C[Try[T]] = {
    projector.both[Try[T]](
      { bf =>
        Try {
          projector.read(bf)(codec)
        }
      },
      {
        _.fold(throw _, projector.write(_)(codec))
      }
    )
  }
  implicit final def TryCodecExp[T](implicit _c: Codec[T]): Codec[Try[T]] = _c
  implicit final def TryReadExp[T](implicit _c: Read[T]): Read[Try[T]] = _c
  implicit final def TryWriteExp[T](implicit _c: Write[T]): Write[Try[T]] = _c

  /**
    * [[Set]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SetCodec[T, C[_]: CodecTypeProjection](codec: C[T]): C[Set[T]] = {
    IterableOnceCodec[T, Set, C](Set.empty, _ + _)(codec)
  }
  implicit final def SetCodecExp[T](implicit codec: Codec[T]): Codec[Set[T]] = codec
  implicit final def SetReadExp[T](implicit codec: Read[T]): Read[Set[T]] = codec
  implicit final def SetWriteExp[T](implicit codec: Write[T]): Write[Set[T]] = codec

  implicit final def ListCodec[T, C[_]: CodecTypeProjection](codec: C[T]): C[List[T]] = {
    IterableOnceCodec[T, List, C](List.empty, _ :+ _)(codec)
  }
  implicit final def ListCodecExp[T](implicit codec: Codec[T]): Codec[List[T]] = codec
  implicit final def ListReadExp[T](implicit codec: Read[T]): Read[List[T]] = codec
  implicit final def ListWriteExp[T](implicit codec: Write[T]): Write[List[T]] = codec

  implicit final def SeqCodec[T, C[_]: CodecTypeProjection](codec: C[T]): C[Seq[T]] = {
    IterableOnceCodec[T, Seq, C](Seq.empty, _ :+ _)(codec)
  }

  implicit final def SeqCodecExp[T](implicit codec: Codec[T]): Codec[Seq[T]] = codec
  implicit final def SeqReadExp[T](implicit codec: Read[T]): Read[Seq[T]] = SeqCodec[T, Read](codec)
  implicit final def SeqWriteExp[T](implicit codec: Write[T]): Write[Seq[T]] = SeqCodec[T, Write](codec)

  implicit final def VectorCodec[T, C[_]: CodecTypeProjection](x: C[T]): C[Vector[T]] = {
    IterableOnceCodec[T, Vector, C](Vector.empty, _ :+ _)(x)
  }

  implicit final def VectorCodecExp[T](implicit codec: Codec[T]): Codec[Vector[T]] = codec
  implicit final def VectorReadExp[T](implicit codec: Read[T]): Read[Vector[T]] = codec
  implicit final def VectorWriteExp[T](implicit codec: Write[T]): Write[Vector[T]] = codec

  /**
    * The base of scala base api codec.
    *
    * @tparam T Target type param.
    */
  sealed trait PrimitiveTypeCodecBase[T] extends Codec[T] {
    override final def deserialize(bf: JsonVal): T = {
      bf match {
        case JsNull => throw IllegalJsonFormatException("Null value cannot deserialize.")
        case _      => parse(bf)
      }
    }

    def fail(bf: JsonVal): Throwable

    def parse(bf: JsonVal): T

    override def serialize(t: T): JsonVal = Json.any(t.toString)
  }

  /**
    * Codec base class for classes that inherit iterable.
    *
    * @param c   Empty value.
    * @param j   Iterable append function.
    * @param tct Inner type parameter codec symbol.
    * @tparam T Inner type parameter.
    * @tparam C Collection types.
    */
  private[refuel] def IterableOnceCodec[T, C[T] <: Iterable[T], D[T]](
                                                                     c: => C[T],
                                                                     j: (C[T], T) => C[T]
                                                                   )(tct: D[T])(implicit projector: CodecTypeProjection[D]): D[C[T]] = {
    def fail(bf: JsonVal): Throwable = {
      CodecDefinitionException(s"Cannot deserialize $bf into ${this.getClass.getName}. Only JsArray or JsObject can be Seq[T] decoded.")
    }

    projector.both[C[T]](
      {
        case JsArray(x) =>
          x.foldLeft(c: C[T]) {
            case (a, b) => j(a, projector.read(b)(tct))
          }
        case JsNull => c
        case bf     => throw fail(bf)
      },
      { x => JsArray(x.map(q => projector.write[T](q)(tct))) }
    )
  }

  /**
    * [[Array]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ArrayCodec[T: ClassTag, C[_]](_x: C[T])(implicit projector: CodecTypeProjection[C]): C[Array[T]] = {
    def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into ${this.getClass.getName}. Only JsArray or JsObject can be Seq[T] decoded.")
    }

    projector.both[Array[T]](
      {
        case JsArray(x) =>
          x.foldLeft[Array[T]](Array.empty) {
            case (a, b) => a.:+(projector.read(b)(_x))
          }
        case JsNull => Array.empty[T]
        case bf     => throw fail(bf)
      },
      t => JsArray(t.map(projector.write(_)(_x)))
    )
  }
  implicit final def ArrayCodecExp[T: ClassTag](implicit codec: Codec[T]): Codec[Array[T]] = codec
  implicit final def ArrayReadExp[T: ClassTag](implicit codec: Read[T]): Read[Array[T]] = codec
  implicit final def ArrayWriteExp[T: ClassTag](implicit codec: Write[T]): Write[Array[T]] = codec

  /**
    * [[Map]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  implicit final def MapCodec[K, V, C[_]](_x: C[(K, V)])(implicit projector: CodecTypeProjection[C]): C[Map[K, V]] = {
    def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into ${this.getClass.getName}. Only JsArray or JsObject can be Seq[T] decoded.")
    }

    projector.both[Map[K, V]](
      {
        case JsObject(x) =>
          Map(
            x.asScala.map {
              case (k, v) =>
                projector.read(Json.arr(k, v))(_x)
            }.toSeq: _*
          )
        case JsNull => Map()
        case bf     => throw fail(bf)
      },
      x =>
        JsObject.pure(
          x.map { entrySet =>
            val tupleEncoded = projector.write(entrySet)(_x).asInstanceOf[JsArray]
            val k = tupleEncoded.n(0)
            val v = tupleEncoded.n(1)
            k match {
              case ks: JsString => ks -> v
              case _ => throw InvalidDeserializationException(s"Map[_, _] serialization key is required be JsString, but was $k")
            }
          }
        )
    )
  }
  implicit final def MapCodecOp[K, V](_x: (Codec[K], Codec[V]))(implicit projector: CodecTypeProjection[Codec]): Codec[Map[K, V]] = __tuple2[K, V](_x._1, _x._2)
  implicit final def MapCodecExp[K, V](implicit codec: Codec[(K, V)]): Codec[Map[K, V]] = codec
//  implicit final def MapCodecOp[K, V](_x: (Read[K], Read[V])): Read[Map[K, V]] = __tuple2[K, V](_x._1, _x._2)
//  implicit final def MapCodecExp[K, V, C[_]](implicit projector: CodecTypeProjection[C], codec: C[(K, V)]): C[Map[K, V]] = codec
//  implicit final def MapCodecOp[K, V](_x: (Codec[K], Codec[V]))(implicit projector: CodecTypeProjection[Codec]): Codec[Map[K, V]] = __tuple2[K, V](_x._1, _x._2)
//  implicit final def MapCodecExp[K, V, C[_]](implicit projector: CodecTypeProjection[C], codec: C[(K, V)]): C[Map[K, V]] = codec

  /**
    * [[Option]] codec generator.
    *
    * @param _x Codec of option param type.
    * @tparam T Inner param type.
    * @return
    */
  implicit final def OptionCodec[T, C[_]](_x: C[T])(implicit projector: CodecTypeProjection[C]): C[Option[T]] = {
    projector.both[Option[T]](
      {
        case JsNull => None
        case bf =>
          Try {
            projector.read(bf)(_x)
          }.toOption
      },
      x => x.fold(JsEmpty: JsonVal)(t => projector.write(t)(_x))
    )
  }
  implicit final def OptionCodecExp[T](implicit codec: Codec[T]): Codec[Option[T]] = codec
  implicit final def OptionCodecExp[T](implicit codec: Read[T]): Read[Option[T]] = codec
  implicit final def OptionCodecExp[T](implicit codec: Write[T]): Write[Option[T]] = codec

  /**
    * [[Either]] type codec generator.
    *
    * @tparam L Inner key param type.
    * @tparam R Inner key param type.
    * @return
    */
  implicit final def EitherCodec[L, R, C[_]](implicit f: C[L], s: C[R], projector: CodecTypeProjection[C]): C[Either[L, R]] = {
    projector.both[Either[L, R]](
      { x => Try(projector.read(x)(s)).fold[Either[L, R]](_ => Left[L, R](projector.read(x)(f)), Right[L, R]) }, {
        case Right(x) => projector.write(x)(s)
        case Left(x)  => projector.write(x)(f)
      }
    )
  }



  implicit final def PrimitiveTypeCodecBaseInt: Codec[Int] = new PrimitiveTypeCodecBase[Int] {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into an Int")
    }

    def parse(bf: JsonVal): Int = Integer.parseInt(bf.pure)
  }

  implicit final def PrimitiveTypeCodecBaseFloat: Codec[Float] = new PrimitiveTypeCodecBase[Float] {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Float")
    }

    def parse(bf: JsonVal): Float = java.lang.Float.parseFloat(bf.pure)
  }

  implicit final def PrimitiveTypeCodecBaseDouble: Codec[Double] = new PrimitiveTypeCodecBase[Double] {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Double")
    }

    def parse(bf: JsonVal): Double = java.lang.Double.parseDouble(bf.pure)
  }

  implicit final def PrimitiveTypeCodecBaseShort: Codec[Short] = new PrimitiveTypeCodecBase[Short] {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Short")
    }

    def parse(bf: JsonVal): Short = java.lang.Short.parseShort(bf.pure)
  }

  implicit final def PrimitiveTypeCodecBaseLong: Codec[Long] = new PrimitiveTypeCodecBase[Long] {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Long")
    }

    def parse(bf: JsonVal): Long = java.lang.Long.parseLong(bf.pure)
  }

  implicit final def PrimitiveTypeCodecBaseBoolean: Codec[Boolean] = new PrimitiveTypeCodecBase[Boolean] {

    def parse(bf: JsonVal): Boolean = bf.pure.toLowerCase match {
      case "1" | "true"  => true
      case "0" | "false" => false
      case _             => throw fail(bf)
    }

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Boolean. Only 1/0 or true/false can be boolean decoded.")
    }
  }

  implicit final def PrimitiveTypeCodecBaseChar: Codec[Char] = new PrimitiveTypeCodecBase[Char] {

    override def serialize(t: Char): JsonVal = JsString(t.toString)

    def parse(bf: JsonVal): Char = bf match {
      case JsAny(x)    => Integer.parseInt(x, 16).toChar
      case JsString(x) => x.head
      case _           => throw fail(bf)
    }

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Char. Only JsAnyVal or JsString can be charactor decoded.")
    }
  }

  //  /**
  //    * [[Vector]] codec generator.
  //    *
  //    * @param _x Codec of collection param type.
  //    * @tparam T Inner param type.
  //    * @return
  //    */
  //  implicit final def VectorCodec[T, C[_]: CodecTyper](_x: C[T]): C[Vector[T]] =
  //    IterableOnceCodec[T, Vector, C](Nil, _ :+ _)(_x)

  implicit final def PrimitiveTypeCodecBaseString: Codec[String] = new PrimitiveTypeCodecBase[String] {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a String")
    }

    override def serialize(t: String): JsonVal = JsString(t)

    def parse(bf: JsonVal): String = bf.pure
  }
}
