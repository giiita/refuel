package refuel.json

import refuel.injector.Injector
import refuel.json.codecs.All
import refuel.json.error.DeserializeFailed
import refuel.json.internal.JsonTokenizer

trait JsParser extends Injector with All {
  private[this] final val _jer = inject[JsonTokenizer]

  protected implicit class JScribe[T](t: T) {
    def toJson(implicit ct: Codec[T]): Json = ct.serialize(t)
  }

  protected implicit class JDescribe(t: String) {
    def as[E](implicit c: Codec[E]): Either[DeserializeFailed, E] = jsonTree.to[E]

    def jsonTree: Json = _jer.run(t)
  }

}
