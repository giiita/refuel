package refuel.json.codecs

import refuel.json.Codec

trait CodecRaiseable[T] {
  def raise: Codec[T]
}
