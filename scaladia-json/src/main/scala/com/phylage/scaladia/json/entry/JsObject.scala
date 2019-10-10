package com.phylage.scaladia.json.entry

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.UnsupportedOperation
import com.phylage.scaladia.json.tokenize.ResultBuff

case class JsObject private[entry](bf: IndexedSeq[(Json, Json)]) extends JsVariable {

  override def toString: String = s"""{${bf.map {
    case (_, JsEmpty) => s""
    case (x, y)       => s"${x.toString()}:${y.toString}"
  }.mkString(",")}}"""

  def ++(js: Json): Json = {
    js match {
      case x: JsObject         => JsObject(bf ++ x.bf)
      case JsEntry(_, JsEmpty) => this
      case JsEntry(key, value) => JsObject(bf.:+(key -> value))
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

  def apply(unbuiltJsons: ResultBuff[Json]): Json = {
    unbuiltJsons.foldLeft[Json](dummy)(_ ++ _)
  }

  def apply(unbuiltJsons: (String, Json)*): Json = {
    unbuiltJsons.foldLeft[Json](dummy)((a, b) => a ++ JsEntry(JsString(b._1), b._2))
  }
}

