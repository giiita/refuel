package refuel.json.entry

import refuel.json.JsonVal
import refuel.json.error.{IllegalJsonSyntaxTreeBuilding, UnsupportedOperation}

case class JsObject private[entry](bf: Seq[(JsString, JsonVal)]) extends JsVariable {
  override def toString: String = {
    var unempty = false
    val b = new StringBuffer()
    b.append('{')
    bf.foreach { x =>
      if (unempty) b.append(", ")
      b.append(x._1)
      b.append(" -> ")
      b.append(x._2)
      if (!unempty) unempty = true
    }
    b.append('}')
    b.toString
  }

  def pour(b: StringBuffer): Unit = {
    var unempty = false
    b.append('{')
    bf.foreach { x =>
      if (unempty) b.append(",")
      x._1.pour(b)
      b.append(':')
      x._2.pour(b)
      if (!unempty) unempty = true
    }
    b.append('}')
  }

  def ++(js: JsonVal): JsonVal = {
    js match {
      case JsNull | null => this
      case x: JsObject =>
        new JsObject(
          (bf ++ x.bf).groupBy(_._1).mapValues(x => x.map(_._2).reduce(_ ++ _)).toSeq
        )
      case JsEmpty => this
      case x => throw UnsupportedOperation(s"Cannot add raw variable element to JsObject. $toString + $x")
    }
  }

  override def named(key: String): JsonVal = {
    bf.collectFirst {
      case (k, v) if k.toString == key => v
    } getOrElse JsNull
  }

  def unapply(arg: JsObject): Option[List[(JsonVal, JsonVal)]] = Some(bf.toList)
}

object JsObject {

  def fromNullableArray(nullableEntries: Seq[(JsString, JsonVal)]): JsonVal = {
    new JsObject(
      nullableEntries.filter {
        case null | (_, JsEmpty) => false
        case _ => true
      }
    )
  }

  private[refuel] def fromEntry(entries: JsonVal*): JsonVal = {
    new JsObject(
      entries.flatMap {
        case JsEntry(k, v) => Some(k -> v)
        case JsObject(v) => v
        case JsEmpty => None
        case other => throw IllegalJsonSyntaxTreeBuilding(s"JSON AST configuration is incorrect. Cannot add to Stream : [ $other ]")
      }.groupBy(_._1).mapValues(x => x.map(_._2).reduce(_ ++ _)).toSeq
    )
  }

  def apply(req: (String, JsonVal)*): JsonVal = {
    new JsObject(
      req.withFilter(_._2 != JsEmpty).map {
        case (x, y) => JsString(x) -> y
      }
    )
  }
}

