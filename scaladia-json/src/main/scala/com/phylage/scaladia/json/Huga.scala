package com.phylage.scaladia.json

import java.io.File

import com.phylage.scaladia.json.codecs.factory.CaseClassCodec
import com.phylage.scaladia.json.tokenize.{AnyValTokenizer, ArrayTokenizer, LiteralTokenizer, ObjectTokenizer}
import org.openjdk.jmh.annotations.{Benchmark, State}
import play.api.libs.json.Json

import scala.io.Source

@State(org.openjdk.jmh.annotations.Scope.Benchmark)
class Huga extends JTransform {

  case class AA(a: String, b: String)

  case class AAA(a: String, b: AA)

  def time[T](f: => T): T = {
    val from = System.currentTimeMillis()
    val r = f
    val to = System.currentTimeMillis()
    println(s"${to - from}")
    r
  }

  val x = Source.fromFile(
    new File("/Users/takagi/src/scaladia/scaladia-json/src/test/resources/test.json")
  ).getLines.mkString

  @Benchmark
  final def exe: Unit = time {
    x.as[Map[String, List[Map[String, String]]]]
    //      o.run(x).sss.to[Map[String, List[Map[String, String]]]]
    //      implicitly[play.api.libs.json.Reads[AAA]].map {
    //        x =>
    //      }
    // implicit val x: Reads[AAA] = Json.reads[AAA]
    // Json.parse(x).validate[Map[String, String]]
  }


  exe
}
