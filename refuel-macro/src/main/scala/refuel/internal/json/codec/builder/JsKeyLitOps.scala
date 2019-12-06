package refuel.internal.json.codec.builder
import refuel.json.Json

trait JsKeyLitOps {
  def v: Seq[String]

  def rec(x: Json): Json

  def ++(that: JsKeyLitOps): JsKeyLitOps
}

object JsKeyLitOps {
  case object SelfCirculationLit extends JsKeyLitOps {
    val v: Seq[String] = Nil

    override def rec(x: Json): Json = x

    def ++(that: JsKeyLitOps): JsKeyLitOps = that
  }
}

