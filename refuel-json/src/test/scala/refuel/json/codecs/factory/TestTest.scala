package refuel.json.codecs.factory

import refuel.json.{Codec, JsParser}

case class From(value: Option[Int])

case class To[T](value: T)

object PixelSizeCodec extends JsParser {

  def apply(): Codec[To[Int]] =
    ConstCodec.from[From, To[Int]]("test") { x =>
      To(x.value.get)
    } { x =>
      Some(From(Some(x.value)))
    }

  // case class PixelSizeDefine(min: Option[Int], max: Option[Int])
}


class TestTest {

}
