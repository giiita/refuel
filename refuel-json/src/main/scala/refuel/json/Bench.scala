package refuel.json

import java.io.File

import org.openjdk.jmh.annotations.{Benchmark, State}
import play.api.libs.json.Json
import refuel.json.Hoge._
import refuel.json.tokenize.JTransformRouter

import scala.io.Source

@State(org.openjdk.jmh.annotations.Scope.Benchmark)
class Bench extends JsContext {
  implicit val _codec: Codec[Root] = "root".parsed(seq(CaseClassCodec.from[Hoge])).apply(Root)(Root.unapply)

  val source = Source.fromFile(
    new File("/Users/takagi/src/refuel/refuel-json/src/test/resources/test.json"),
    "UTF-8"
  ).getLines().mkString

  val length = source.length()

  //  var index = 0
  //  def source() = {
  //    index = index + 1
  //    s"""{"index": $index}"""
  //  }

  implicit val codec1 = Json.reads[Name]
  implicit val codec2 = Json.reads[Friend]
  implicit val codec3 = Json.reads[Hoge]
  implicit val codec4 = Json.reads[Root]

  val jt = new JTransformRouter(source)

  @Benchmark
  def dec = {
    // val from = System.currentTimeMillis()

    //    (1 to 10000).foreach(_ => Json.parse(source))
    // Json.parse(source).validate[Root]

    // hoo(source.toCharArray)
    //    val x = new StringReader(source)
    //    loop(x)
    //
    //    @tailrec
    //    def loop(v: StringReader): Unit = {
    //      if (v.read() != -1) {
    //        loop(v)
    //      }
    //    }
    // Serial.run(source)
    // println(s"""${System.currentTimeMillis() - from}""")

    // source.jsonTree
    jt.jsonTree // .to[Root]// .to[Root]
    //    ObjectTokenizer.run(source)
  }

  val from = System.currentTimeMillis()
  dec
  println(s"""${System.currentTimeMillis() - from}""")
}