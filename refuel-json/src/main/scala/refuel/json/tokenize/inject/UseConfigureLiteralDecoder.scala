package refuel.json.tokenize.inject

import refuel.injector.Injector
import refuel.json.conf.JsonConf
import refuel.json.tokenize.JsonStreamingTokenizer

trait UseConfigureLiteralDecoder extends Injector {
//   private[this] val jsonConf = inject[JsonConf]
//  lazy val literalTokenizer: JsonStreamingTokenizer[Char, Char => Boolean] =
//    ??? // if (jsonConf.unicodeDeserialize) UnicodeLiteralDecoder else LiteralTokenizer
}
