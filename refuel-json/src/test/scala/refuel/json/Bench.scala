//package refuel.json
//
//import java.io.File
//
//import org.openjdk.jmh.annotations.{Benchmark, State}
//
//import scala.io.Source
//
//@State(org.openjdk.jmh.annotations.Scope.Benchmark)
//class Bench extends JsContext {
//
//  implicit val _codec: Codec[Root] = "root".parsed(seq(CaseClassCodec.from[Hoge])).apply(Root)(Root.unapply)
//
//  val source = Source.fromFile(
//    new File("/Users/takagi/src/refuel/refuel-json/src/main/resources/test.json"),
//    "UTF-8"
//  ).getLines().mkString
//
//  //  implicit val codec1 = Json.reads[Name]
//  //  implicit val codec2 = Json.reads[Friend]
//  //  implicit val codec3 = Json.reads[Hoge]
//  //  implicit val codec4 = Json.reads[Root]
//
//  @Benchmark
//  def dec = {
//    //    Json.parse(source).validate(codec4) match {
//    //      case x: JsSuccess[_] =>
//    //      case e =>
//    //    }
//    source.as[Root] match {
//      case Right(_) => ()
//      case Left(e) => e.printStackTrace()
//    }
//  }
//}