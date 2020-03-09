package refuel.json

import refuel.json.error.DeserializeFailed
import refuel.json.tokenize.JTransformRouter

/**
 * Context that performs Json serialize / deserialize by refuel json.
 */
trait JsonTransform {

  /**
   * Serialize any object to Json syntax tree.
   * In this state, it is not JsonRawData, but it becomes JsonRawData by [[Json.pour]].
   * A function that takes an implicit codec, but in many cases it will require explicit assignment.
   * {{{
   *   ???.toJson(CaseClassCodec.from[XXX])
   * }}}
   *
   * @param t Any object
   * @tparam T Any object type
   */
  protected implicit class JScribe[T](t: T) {
    def toJson(implicit ct: Codec[T]): Json = ct.serialize(t)

    def toJString(implicit ct: Codec[T]): String = {
      val buf = new StringBuffer()
      toJson.pour(buf)
      buf.toString
    }
  }

  /**
   * Deserialize JsonRawData.
   * If you want to build a Json syntax tree, call [[jsonTree]]
   * Otherwise, return deserialize result with failure in [[as(CodecClassCodec.from[XXX])]].
   *
   * There are currently three main ways to generate Codec.
   *
   *
   * 1. [[CaseClassCodec]]
   * {{{
   *     CaseClassCodec.from[XXX]
   * }}}
   * Applying class and unapply function are required to use CaseClassCodec.
   * Also, the JsonRawData key and the case class field name must exactly match. Order has no effect.
   *
   *
   * 2. [[ConstCodec]]
   * {{{
   *     ConstCodec.from("key1", "key2")(XXX.apply)(XXX.unapply)
   * }}}
   * The variable length character string that becomes the first argument corresponds to the key of Json raw data.
   * Cannot refer to class field name due to dynamic apply function.
   *
   * NOTE: In addition, due to the nature of macro, it is not possible to transfer the argument of the upper function as the argument of the macro function.
   *
   *
   * 3. Custom codec builder
   * {{{
   *     "root".parsed(
   *       {
   *         "1/4".parsed(CodecA) ++
   *           "2/4".parsed(CodecA) ++
   *           "3/4".parsed(option(CodecAA)) ++
   *           "4/4".parsed(option(CodecAA))
   *         }.apply(B.apply)(B.unapply)
   *     ).apply(C.apply)(C.unapply)
   * }}}
   * You can use DSL to combine Codec and generate new Codecs.
   *
   * @param t Json literal
   */
  protected implicit class JDescribe(t: String) {
    def as[E](implicit c: Codec[E]): Either[DeserializeFailed, E] = jsonTree.to[E]

    def jsonTree: Json = new JTransformRouter(t).jsonTree
  }
}
