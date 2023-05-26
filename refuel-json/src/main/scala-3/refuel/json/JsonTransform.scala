package refuel.json

import refuel.json.`macro`.TypedCodecMacro
import refuel.json.codecs.*
import refuel.json.codecs.IterableCodecTranslator
import refuel.json.compiletime.DeriveCodecs
import refuel.json.conversion.JsonRowEntry
import refuel.json.exception.CodecDefinitionException
import refuel.json.spec.JsonKeyStructure
import refuel.json.tokenize.decoded.JsonRaw

import scala.deriving.Mirror
import scala.util.Try

object JsonTransform extends JsonTransform {
}
trait JsonTransform extends TupleCodecsImpl with IterableCodecTranslator with PredefCodecs with ImplicitCodecDefSupport with RwProjection {
  export JsonTransform.given

  /** Refuel internal API.
    * Derives a list of codecs for elements of any tuple type.
    *
    * @tparam T
    * @return
    */
  inline final def TupleCodecDerivation[T <: Tuple]: Tuple.Map[T, Codec] = ${TypedCodecMacro.tupleDerivings[T]}

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
  inline final def Derive[T]: Codec[T] = ${TypedCodecMacro.derivings[T]}

  /** Derive the Codec in Compiletime.
    * The solution phase is different from Derive; CaseClass is more stable.
    * However, it cannot be used for Value classes that inherit from AnyVal, or for Opaque types. In that case, please use Derive.
    *
    * @tparam T Case class type
    * @return
    */
  inline final def CaseClass[T]: Codec[T] = DeriveCodecs.inferCodec[T]

  /** Create a Deserializer from Constructor.
    *
    * @param const
    * @tparam T
    * @return
    */
  inline final def Deserialize[T](const: JsonVal => Try[T]): Read[T] = Read[T](const)

  /** Create a Serializer from Destructor.
    *
    * @param dest
    * @tparam T
    * @return
    */
  inline final def Serialize[T](dest: T => Try[JsonVal]): Write[T] = Write[T](dest)

  /** Create a Codec from Constructor and Destructor.
    *
    * @param const
    * @param dest
    * @tparam T
    * @return
    */
  inline final def Construct[T](const: JsonVal => Try[T], dest: T => Try[JsonVal]) = Codec.apply[T](const, dest)

  given JsonRowToEntry: scala.Conversion[String, JsonRowEntry] = new JsonRaw(_)
  
  override given codecTypeProjectionRead: CodecTypeProjection[Read] = new CodecTypeProjection[Read] { me =>
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

  override given codecTypeProjectionWrite: CodecTypeProjection[Write] = new CodecTypeProjection[Write] { me =>
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
}
