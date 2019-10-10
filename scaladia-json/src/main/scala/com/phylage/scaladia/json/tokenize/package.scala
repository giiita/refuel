package com.phylage.scaladia.json

import java.io.StringReader

import scala.collection.mutable.ArrayBuffer

package object tokenize {


  type ResultBuff[T] = ArrayBuffer[T]
  type ReadStream = StringReader

  implicit final val RootTokenizer: Tokenizer[Json, Json, Char] = ObjectTokenizer
}
