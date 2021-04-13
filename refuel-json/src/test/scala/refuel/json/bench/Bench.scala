package refuel.json.bench

import java.io.File
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import refuel.json.bench.Foo._
import refuel.json.tokenize.decoded.DecodedJsonRaw
import refuel.json.{Codec, CodecDef, JsonTransform}

import scala.io.Source

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(
  value = 1,
  jvmArgs = Array(
    "-server",
    "-Xms1g",
    "-Xmx1g",
    "-XX:NewSize=512m",
    "-XX:MaxNewSize=512m",
    "-XX:InitialCodeCacheSize=256m",
    "-XX:ReservedCodeCacheSize=256m",
    "-XX:+UseParallelGC",
    "-XX:-UseAdaptiveSizePolicy",
    "-XX:MaxInlineLevel=18",
    "-XX:-UseBiasedLocking",
    "-XX:+AlwaysPreTouch",
    "-XX:+UseNUMA",
    "-XX:-UseAdaptiveNUMAChunkSizing"
  )
)
class Bench extends JsonTransform with CodecDef {
  val xxx                          = CaseClassCodec.from[Hoge]
  implicit val _codec: Codec[Root] = "root".parsed(seq(CaseClassCodec.from[Hoge])).apply(Root.apply)(Root.unapply)

  val source = Source
    .fromFile(
      new File("/Users/giiita/src/refuel/refuel-json/src/test/resources/test.json"),
      "UTF-8"
    )
    .getLines()
    .mkString

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

  val jt = new DecodedJsonRaw(source)

//  val p = Json.parse(source).validate[Root]
//
//  @Benchmark
//  def playSerialize = {
//
//    Json.stringify(Json.toJson(p.get))
//  }

  val r = jt.jsonTree.des[Root]

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
    jt.jsonTree.des[Root]
  }
}
