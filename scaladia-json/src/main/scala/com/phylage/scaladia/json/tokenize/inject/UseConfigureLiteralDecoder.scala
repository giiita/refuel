package com.phylage.scaladia.json.tokenize.inject

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.json.conf.JsonConf
import com.phylage.scaladia.json.tokenize.JsonStreamingTokenizer

trait UseConfigureLiteralDecoder extends Injector {
//   private[this] val jsonConf = inject[JsonConf]
//  lazy val literalTokenizer: JsonStreamingTokenizer[Char, Char => Boolean] =
//    ??? // if (jsonConf.unicodeDeserialize) UnicodeLiteralDecoder else LiteralTokenizer
}
