package refuel.json

import refuel.json.`macro`.TypedCodecMacro
import refuel.json.codecs._
import refuel.json.conversion.JsonRowEntry
import refuel.json.exception.CodecDefinitionException
import refuel.json.tokenize.decoded.JsonRaw

import scala.util.Try

/**
  * Deserialization always builds an AST equivalent to the input and does not do any JSON DECODE of the escape character.
  *
  * Serialization always enforces JSON ENCODE.
  * For example, if a JSON string contains a line break, the serialized result will always be a "\\n".
  *
  * If you need JSON DECODE, use [[EncodedJsonTransform]].
  *
  * When used with an HTTP Server or Client, the communication target may not support JSON ENCODE / DECODE.
  * In this case, it is safer to use EncodedJsonTransform. However, in such cases, if you want to input a backslash
  * as a string, the behavior is not intended, so if this is a problem, check the destination's response/reception format
  * and use an appropriate Transformer.
  */
object JsonTransform extends JsonTransform
trait JsonTransform extends TupleCodecsImpl with IterableCodecTranslator with PredefCodecs with ImplicitCodecDefSupport with RwProjection {

  override implicit def codecTypeProjectionRead: CodecTypeProjection[Read] = new CodecTypeProjection[Read] { me =>
    def wrap[T](key: JsonKeySpec)(implicit codec: Read[T]): Read[T] = new Read[T] {
      def deserialize(bf: JsonVal): T = codec.deserialize(key.dig(bf))
    }

    override def read[T](fn: JsonVal)(implicit codec: Read[T]): T = codec.deserialize(fn)

    override def write[T](fn: T)(implicit codec: Read[T]): JsonVal =
      throw CodecDefinitionException(s"I am deserialization only. Cannot serialize ${fn.toString}")

    override def both[T](_read: JsonVal => T, _write: T => JsonVal): Read[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T = _read(bf)
      override def serialize(t: T): JsonVal =
        throw CodecDefinitionException(s"I am deserialization only. Cannot serialize ${t.toString}")
    }
  }

  override implicit def codecTypeProjectionWrite: CodecTypeProjection[Write] = new CodecTypeProjection[Write] { me =>
    def wrap[T](key: JsonKeySpec)(implicit codec: Write[T]): Write[T] = new Write[T] {
      def serialize(t: T): JsonVal = key(codec.serialize(t))
    }

    override def read[T](fn: JsonVal)(implicit codec: Write[T]): T =
      throw CodecDefinitionException(s"I am serialization only. Cannot deserialize ${fn.toString}")

    override def write[T](fn: T)(implicit codec: Write[T]): JsonVal =
      codec.serialize(fn)

    override def both[T](_read: JsonVal => T, _write: T => JsonVal): Write[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T =
        throw CodecDefinitionException(s"I am serialization only. Cannot deserialize ${bf.toString}")
      override def serialize(t: T): JsonVal = _write(t)
    }
  }

  /** Create a Deserializer from Constructor.
    *
    * @param const
    * @tparam T
    * @return
    */
  final def Deserialize[T](const: JsonVal => Try[T]): Read[T] = Read[T](const)

  /** Create a Serializer from Destructor.
    *
    * @param dest
    * @tparam T
    * @return
    */
  final def Serialize[T](dest: T => Try[JsonVal]): Write[T] = Write[T](dest)

  implicit final def JsonRowToEntry(v: String): JsonRowEntry = new JsonRaw(v)

  /** Create a Codec from Constructor and Destructor.
    *
    * @param const
    * @param dest
    * @tparam T
    * @return
    */
  final def Construct[T](const: JsonVal => Try[T], dest: T => Try[JsonVal]) = Codec.apply[T](const, dest)

  /** Derive Codec from AST. If a reference to the relevant implicit Codec exists in the scope, use it as much as possible.
    * Even if it is not a case class, it can be used if apply is defined in the companion object.
    *
    * This is solved in the following order.
    * 1. implicit `Codec[T]` instance
    * 2. implicit conversion of `Codec[T] => Codec[K[T]]`.
    * 3. implicit conversion of `Tuple.Map[T <: Tuple, Codec] => Codec[T]`
    * 4. signature analysis of `Product` subtypes
    *
    * 1. implicit Codec[T] instance
    *    If there is already an implicit Codec[T], and Derive binds it to another
    *    implicit property, an infinite loop may occur and it may not compile.
    *
    *    opaque type codec derivation must be in the scope of opaque type.
    *
    * @tparam T
    * @return
    */
  final def Derive[T]: Codec[T] = macro TypedCodecMacro.fromInferOrCase[T]
}
