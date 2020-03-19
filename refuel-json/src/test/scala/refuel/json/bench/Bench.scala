package refuel.json.bench

import java.io.File

import org.openjdk.jmh.annotations.{Benchmark, State}
import refuel.json.bench.Foo._
import refuel.json.tokenize.JsonTransformRouter
import refuel.json.{Codec, CodecDef, JsonTransform}

import scala.io.Source

@State(org.openjdk.jmh.annotations.Scope.Benchmark)
class Bench extends JsonTransform with CodecDef {
  implicit val _codec: Codec[Root] = "root".parsed(seq(CaseClassCodec.from[Hoge])).apply(Root.apply)(Root.unapply)

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

//  implicit val codec1 = Json.reads[Name]
//  implicit val codec2 = Json.reads[Friend]
//  implicit val codec3 = Json.reads[Hoge]
//  implicit val codec4 = Json.reads[Root]
//
//  implicit val codec5 = Json.writes[Name]
//  implicit val codec6 = Json.writes[Friend]
//  implicit val codec7 = Json.writes[Hoge]
//  implicit val codec8 = Json.writes[Root]

  val jt = new JsonTransformRouter(source)


//  val p = Json.parse(source).validate[Root]
//
//  @Benchmark
//  def playSerialize = {
//
//    Json.stringify(Json.toJson(p.get))
//  }

  val r = jt.jsonTree.to[Root]

  @Benchmark
  def refuelSerialize = {
    r.toJString
  }

//  @Benchmark
//  def playDeserialize = {
//    Json.parse(source).validate[Root].get
//  }

  @Benchmark
  def refuelDeserialize = {
    jt.jsonTree.to[Root]
  }
}