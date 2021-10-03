package refuel.json.codecs

import refuel.json.JsonVal.*
import refuel.json.codecs.datetime.DateTimeConversionStrategy
import refuel.json.exception.{CodecDefinitionException, IllegalJsonFormatException, InvalidDeserializationException}
import refuel.json.{Json, JsonTransform, JsonVal}
import refuel.lang.RuntimeTZ
import collection.JavaConverters.mapAsScalaMapConverter

import java.time.ZonedDateTime
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

trait PredefCodecs { this: JsonTransform =>

  import scala.language.implicitConversions
  
  given JsonValCodec: Codec[JsonVal] = new Codec[JsonVal] {
    override def serialize(t: JsonVal): JsonVal    = t
    override def deserialize(bf: JsonVal): JsonVal = bf
  }
  given UnitCdc: Codec[Unit] = new Codec[Unit] {
    override def serialize(t: Unit): JsonVal    = JsEmpty
    override def deserialize(bf: JsonVal): Unit = ()
  }

  given ZonedDateTimeCdc(
      using rt: RuntimeTZ,
      strategy: DateTimeConversionStrategy = DateTimeConversionStrategy.Formatter
  ): Codec[ZonedDateTime] =
    new Codec[ZonedDateTime] {
      override def serialize(t: ZonedDateTime): JsonVal = strategy.serialize(t).fold(throw _, x => x)

      override def deserialize(bf: JsonVal): ZonedDateTime = {
        strategy.deserialize(bf).fold(throw _, x => x)
      }
    }

  given TryCodec[T, C[_]](using projector: CodecTypeProjection[C]): scala.Conversion[C[T], C[Try[T]]] = { codec =>
    projector.both(
      { bf =>
        Try {
          projector.read(bf)(using codec)
        }
      },
      {
        _.fold(throw _, projector.write(_)(using codec))
      }
    )
  }
  given TryCdcExp[T](using _c: Codec[T]): Codec[Try[T]] = TryCodec[T, Codec].apply(_c)

  /**
    * [[Set]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  given SetCodec[T, C[_]: CodecTypeProjection]: scala.Conversion[C[T], C[Set[T]]] = {
    IterableOnceCodec[T, Set, C](Set.empty, _ + _)(_)
  }
  given SetCodecExp[T, C[_]: CodecTypeProjection](using codec: C[T]): C[Set[T]] = codec

  given ListCodec[T, C[_]: CodecTypeProjection]: scala.Conversion[C[T], C[List[T]]] = {
    IterableOnceCodec[T, List, C](List.empty, _ :+ _)(_)
  }
  given ListCodecExp[T, C[_]: CodecTypeProjection](using codec: C[T]): C[List[T]] = codec

  given SeqCodecExp[T](using codec: Codec[T]): Codec[Seq[T]] = SeqCodec[T, Codec].apply(codec)

  given SeqReadExp[T](using codec: Read[T]): Read[Seq[T]] = SeqCodec[T, Read].apply(codec)

  given SeqWriteExp[T](using codec: Write[T]): Write[Seq[T]] = SeqCodec[T, Write].apply(codec)

  given SeqCodec[T, C[_]: CodecTypeProjection]: scala.Conversion[C[T], C[Seq[T]]] = {
    IterableOnceCodec[T, Seq, C](Seq.empty, _ :+ _)(_)
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
                                                                   )(tct: D[T])(using projector: CodecTypeProjection[D]): D[C[T]] = {
    def fail(bf: JsonVal): Throwable = {
      CodecDefinitionException(s"Cannot deserialize $bf into ${this.getClass.getName}. Only JsArray or JsObject can be Seq[T] decoded.")
    }

    projector.both[C[T]](
      {
        case JsArray(x) =>
          x.foldLeft(c: C[T]) {
            case (a, b) => j(a, projector.read(b)(using tct))
          }
        case JsNull => c
        case bf     => throw fail(bf)
      },
      { x => JsArray(x.map(q => projector.write[T](q)(using tct))) }
    )
  }

  given VectorCodec[T, C[_]: CodecTypeProjection]: scala.Conversion[C[T], C[Vector[T]]] = {
    IterableOnceCodec[T, Vector, C](Vector.empty, _ :+ _)(_)
  }

  given VectorCodecExp[T, C[_]: CodecTypeProjection](using codec: C[T]): C[Vector[T]] = codec

  /**
    * [[Array]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam T Inner param type.
    * @return
    */
  given ArrayCodec[T: ClassTag, C[_]](using projector: CodecTypeProjection[C]): scala.Conversion[C[T], C[Array[T]]] = { _x =>
    def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into ${this.getClass.getName}. Only JsArray or JsObject can be Seq[T] decoded.")
    }

    projector.both[Array[T]](
      {
        case JsArray(x) =>
          x.foldLeft[Array[T]](Array.empty) {
            case (a, b) => a.:+(projector.read(b)(using _x))
          }
        case JsNull => Array.empty[T]
        case bf     => throw fail(bf)
      },
      t => JsArray(t.map(projector.write(_)(using _x)))
    )
  }
  given ArrayCodecExp[T: ClassTag, C[_]](using projector: CodecTypeProjection[C], codec: C[T]): C[Array[T]] = codec

  /**
    * [[Map]] codec generator.
    *
    * @param _x Codec of collection param type.
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  given MapCodec[K, V, C[_]](using projector: CodecTypeProjection[C]): scala.Conversion[C[(K, V)], C[Map[K, V]]] = { _x =>
    def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into ${this.getClass.getName}. Only JsArray or JsObject can be Seq[T] decoded.")
    }

    projector.both[Map[K, V]](
      {
        case JsObject(x) =>
          Map(
            x.asScala.map {
              case (k, v) =>
                projector.read(Json.arr(k, v))(using _x)
            }.toSeq: _*
          )
        case JsNull => Map()
        case bf     => throw fail(bf)
      },
      x =>
        JsObject.pure(
          x.map { entrySet =>
            val tupleEncoded = projector.write(entrySet)(using _x).asInstanceOf[JsArray]
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
  given MapCodecExp[K, V, C[_]](using projector: CodecTypeProjection[C], codec: C[(K, V)]): C[Map[K, V]] = codec

  /**
    * [[Option]] codec generator.
    *
    * @param _x Codec of option param type.
    * @tparam T Inner param type.
    * @return
    */
  given OptionCodec[T, C[_]](using projector: CodecTypeProjection[C]): scala.Conversion[C[T], C[Option[T]]] = { _x =>
    projector.both[Option[T]](
      {
        case JsNull => None
        case bf =>
          Try {
            projector.read(bf)(using _x)
          }.toOption
      },
      x => x.fold(JsEmpty: JsonVal)(t => projector.write(t)(using _x))
    )
  }
  given OptionCodecExp[T, C[_]](using projector: CodecTypeProjection[C], codec: C[T]): C[Option[T]] = codec

  /**
    * [[Either]] type codec generator.
    *
    * @tparam L Inner key param type.
    * @tparam R Inner key param type.
    * @return
    */
  given EitherCodec[L, R, C[_]](using f: C[L], s: C[R], projector: CodecTypeProjection[C]): C[Either[L, R]] = {
    projector.both[Either[L, R]](
      { x => Try(projector.read(x)(using s)).fold[Either[L, R]](_ => Left[L, R](projector.read(x)(using f)), Right[L, R]) }, {
        case Right(x) => projector.write(x)(using s)
        case Left(x)  => projector.write(x)(using f)
      }
    )
  }

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

  given PrimitiveTypeCodecBase[Int] with {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into an Int")
    }

    def parse(bf: JsonVal): Int = Integer.parseInt(bf.pure)
  }

  given PrimitiveTypeCodecBase[Float] with {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Float")
    }

    def parse(bf: JsonVal): Float = java.lang.Float.parseFloat(bf.pure)
  }

  given PrimitiveTypeCodecBase[Double] with {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Double")
    }

    def parse(bf: JsonVal): Double = java.lang.Double.parseDouble(bf.pure)
  }

  given PrimitiveTypeCodecBase[Short] with {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Short")
    }

    def parse(bf: JsonVal): Short = java.lang.Short.parseShort(bf.pure)
  }

  given PrimitiveTypeCodecBase[Long] with {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Long")
    }

    def parse(bf: JsonVal): Long = java.lang.Long.parseLong(bf.pure)
  }

  given PrimitiveTypeCodecBase[Boolean] with {

    def parse(bf: JsonVal): Boolean = bf.pure.toLowerCase match {
      case "1" | "true"  => true
      case "0" | "false" => false
      case _             => throw fail(bf)
    }

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a Boolean. Only 1/0 or true/false can be boolean decoded.")
    }
  }

  given PrimitiveTypeCodecBase[Char] with {

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

  given PrimitiveTypeCodecBase[String] with {

    override def fail(bf: JsonVal): Throwable = {
      IllegalJsonFormatException(s"Cannot deserialize $bf into a String")
    }

    override def serialize(t: String): JsonVal = JsString(t)

    def parse(bf: JsonVal): String = bf.pure
  }
}
