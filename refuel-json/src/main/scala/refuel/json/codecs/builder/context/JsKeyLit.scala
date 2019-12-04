package refuel.json.codecs.builder.context
import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.{Codec, Json}
import refuel.json.codecs.builder.CBuildComp

case class JsKeyLit(v: Seq[String]) extends JsKeyLitOps {
  def /(add: String): JsKeyLit = JsKeyLit(v :+ add)
  def map[A: Codec] = new CBuildComp(this)

  override def rec(x: Json): Json = {
    v.foldLeft(x)(_ named _)
  }

  override def toString: String = v.mkString
}
