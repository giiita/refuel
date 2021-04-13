package refuel.json

import refuel.json.codecs.{Read, Write}

sealed trait ReadableTyper[T] {
  def read(json: JsonVal): T
}
object ReadableTyper {
  implicit def xxx[T](implicit reader: Read[T]): ReadableTyper[T] = new ReadableTyper[T] {
    def read(json: JsonVal): T = reader.deserialize(json)
  }
//  implicit def yyy[T](implicit reader: Codec[T]): ReadableTyper[T] = new ReadableTyper[T] {
//    def read(json: JsonVal): T = reader.deserialize(json)
//  }
}

sealed trait WriteableTyper[T[_]]
object WriteableTyper {
  case object WriteableTyperWriter extends WriteableTyper[Write]
  case object WriteableTyperCodec  extends WriteableTyper[Codec]
}
