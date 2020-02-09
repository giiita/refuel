package refuel.json

import org.openjdk.jmh.annotations.State
import refuel.json.entry.JsObject
import refuel.json.internal.JsonCodeMap.{COLON, COMMA}
import refuel.json.tokenize.{JsonStreamingTokenizer, ReadStream, ResultBuff}

import scala.annotation.tailrec
import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer

@State(org.openjdk.jmh.annotations.Scope.Benchmark)
class CollectionTest extends JsonStreamingTokenizer {

  val x = (1 to 30000).map(_ => 'a').toArray
  val xLength = x.length
  val sb = new Array[Char](xLength)

  val r = new ArrayBuffer[Int]()

  // @Benchmark
  def dec = {
    //    new String(x)
    tailing(0)
    new String(sb, 5000, 20000)
    //    sb.mkString
  }

  var xxx: Json = JsObject.dummy

  val SKIP = BitSet(((1 to 32) ++ Seq(COMMA.toInt, COLON.toInt)): _*)
  val where = xx => SKIP.contains(xx)

  @tailrec
  final def tailing(i: Int): Unit = {

    //    @tailrec
    //    def inner(n: Int): Int = {
    //      if (n == 100) i + n else inner(n + 1)
    //    }
    //
    //    xxx.++(JsNull)
    //    indexWhere(x, where, i
    //    )
    if (i < xLength) {
      sb(i) = x(i)
      tailing(i + 1)
    }
  }

  override def takeMap(i: Int, rs: ReadStream, rb: ResultBuff[Json]): Int = ???

  override def run(v: ReadStream): Json = ???
}
