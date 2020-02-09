package refuel.json

package object tokenize {

  type ResultBuff[T] = T
  type ReadStream = Array[Char]

  implicit final val RootTokenizer: Tokenizer[Json, Int] = ObjectTokenizer
}
