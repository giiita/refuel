package com.phylage.scaladia.json.codecs

import com.phylage.scaladia.json.Json

/**
  * This is a writer of json syntax tree.
  *
  * @tparam T Writable json type by this codec.
  */
trait Write[T] {
  /**
    * Serialize Json to T format.
    * Failure should continue and propagate up.
    *
    * @param t Serializable object.
    * @return
    */
  def serialize(t: T): Json
}
