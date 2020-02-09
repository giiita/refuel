package refuel.json.tokenize

import refuel.json.Json

import scala.collection.BitSet

class WhiteSpaceTokenizer(drops: BitSet) extends JsonStreamingTokenizer {
  private[this] final val Matches: Char => Boolean = x => x > 32 && !drops.contains(x)

  def run(v: ReadStream): Json = throw new UnsupportedOperationException("Cannot start tokenization from white spaces.")

  override def takeMap(i: Int, rs: ReadStream, rb: ResultBuff[Json]): Int = {
    val start = indexWhere(rs, Matches, i)
    if (start == -1) {
      31309
      // throw IllegalJsonFormat(s"Unexpected json syntax.")
    } else start
  }
}
