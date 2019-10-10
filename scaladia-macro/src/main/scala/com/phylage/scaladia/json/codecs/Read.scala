package com.phylage.scaladia.json.codecs

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.DeserializeFailed

/**
  * This is a reader of json syntax tree.
  *
  * @tparam T Readable json type by this codec.
  */
trait Read[T] {
  /**
    * Deserialize Json to T format.
    * Failure should continue and propagate up.
    *
    * @param bf Json syntax tree.
    * @return
    */
  def deserialize(bf: Json): Either[DeserializeFailed, T]
}
