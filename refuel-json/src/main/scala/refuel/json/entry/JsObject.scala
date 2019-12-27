package refuel.json.entry

import refuel.json.Json
import refuel.json.error.UnsupportedOperation
import refuel.json.tokenize.ResultBuff

case class JsObject private[entry](bf: IndexedSeq[(JsLiteral, Json)]) extends JsVariable {

  override def toString: String = s"""{${bf.map {
    case (_, JsEmpty) => s""
    case (x, y)       => s"${x.toString()}:${y.toString}"
  }.mkString(",")}}"""

  override def prettyprint: String = s"{\n${bf.map {
    case (_, JsEmpty) => ""
    case (x, y)       => s"  ${x.toString()}: ${y.toString}"
  }.mkString(",\n")}}"

  def ++(js: Json): Json = {
    js match {
      case x: JsObject         =>
        x.bf.foldLeft[Json](this) {
          case (a, b) => a ++ JsEntry(b._1, b._2)
        }
      case JsEntry(_, JsEmpty) => this
      case JsEntry(key, value) =>
        JsObject {
          bf.find(_._1 == key)
          bf.partition(_._1 == key) match {
            case (found, _) if found.isEmpty => bf.:+(key -> value)
            case (found +: x, others) => others :+ {
              key -> {
                x.foldLeft(found._2) {
                  case (a, b) => a ++ b._2
                } ++ value
              }
            }
          }
        }
      case x: JsKeyBuffer      => JsKeyBuffer(x.bf, ++(x.jso))
      case x: JsLiteral        => x.toKey(this)
      case JsEmpty             => this
      case x                   => throw UnsupportedOperation(s"Cannot add raw variable element to JsObject. $toString +>> ${x.getClass}")
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

  def dummy = new JsObject(IndexedSeq.empty)

  def apply(unbuiltJsons: Iterable[Json]): Json = {
    unbuiltJsons.foldLeft[Json](dummy)(_ ++ _)
  }

  def apply(unbuiltJsons: ResultBuff[Json]): Json = {
    this.apply(unbuiltJsons.toList)
  }

  def apply(unbuiltJsons: (String, Json)*): Json = {
    unbuiltJsons.foldLeft[Json](dummy)((a, b) => a ++ JsEntry(JsString(b._1), b._2))
  }
}

