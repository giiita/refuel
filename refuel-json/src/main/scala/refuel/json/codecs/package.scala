package refuel.json

import refuel.json.codecs.builder.context.CodecDefinitionContext

package object codecs extends CodecDefinitionContext {
  implicit def __readRef[T](implicit _c: Codec[T]): Read[T]   = _c
  implicit def __writeRef[T](implicit _c: Codec[T]): Write[T] = _c
}
