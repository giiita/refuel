package com.phylage.scaladia.json

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.json.codecs.All
import com.phylage.scaladia.json.error.DeserializeFailed
import com.phylage.scaladia.json.internal.JsonTokenizer

trait JTransform extends Injector with All {
  private[this] final val _jer = inject[JsonTokenizer]

  protected implicit class JScribe[T](t: T) {
    def toJson(implicit ct: Codec[T]): Json = ct.serialize(t)
  }

  protected implicit class JDescribe(t: String) {
    def as[E](implicit c: Codec[E]): Either[DeserializeFailed, E] = jsonTree.to[E]

    def jsonTree: Json = _jer.run(t)
  }

}
