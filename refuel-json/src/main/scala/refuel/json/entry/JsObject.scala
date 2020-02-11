package refuel.json.entry

import refuel.json.Json
import refuel.json.error.UnsupportedOperation

import scala.annotation.switch

case class JsObject private[entry](bf: Seq[(JsString, Json)]) extends JsVariable {

  override def toString: String =
    s"""{${
      bf.map {
        case (_, JsEmpty) => s""
        case (x, y) => s"${x.toString()}:${y.toString}"
      }.mkString(",")
    }}"""

  override def prettyprint: String = s"{\n${
    bf.map {
      case (_, JsEmpty) => ""
      case (x, y) => s"  ${x.toString()}: ${y.toString}"
    }.mkString(",\n")
  }}"

  def ++(js: Json): Json = {
    (js: @switch) match {
      case x: JsString => x.toKey(this)
      case x: JsKeyBuffer => JsKeyBuffer(x.bf, ++(x.jso))
      case JsNull => this
      case x: JsObject =>
        x.bf.foldLeft[Json](this) {
          case (a, b) => a ++ JsEntry(b._1, b._2)
        }
      case JsEmpty => this
      case JsEntry(_, JsEmpty) => this
      case JsEntry(key, value) =>
        JsObject {
          bf.partition(_._1 == key) match {
            case (found, _) if found.isEmpty => bf.:+(key -> value)
            case (found +: x, others) => others :+ {
              key -> {
//                x.foldLeft(found._2) {
//                  case (a, b) => a ++ b._2
//                } ++ value
                value
              }
            }
          }
        }
      case x => throw UnsupportedOperation(s"Cannot add raw variable element to JsObject. $toString +>> ${x.getClass}")
    }
  }

  override def isIndependent: Boolean = true

  override def named(key: String): Json = {
    bf.collectFirst {
      case (k, v) if k.unquote == key => v
    } getOrElse JsNull
  }

  def unapply(arg: JsObject): Option[List[(Json, Json)]] = Some(bf.toList)
}

object JsObject {

  lazy val dummy = new JsObject(Nil)

  def apply(unbuiltJsons: Iterable[Json]): Json = {
    unbuiltJsons.foldLeft[Json](dummy)(_ ++ _)
  }

  def apply(unbuiltJsons: (String, Json)*): Json = {
    unbuiltJsons.foldLeft[Json](dummy)((a, b) => a ++ JsEntry(JsString(b._1), b._2))
  }
}

