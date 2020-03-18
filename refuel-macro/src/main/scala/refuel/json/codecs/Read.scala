package refuel.json.codecs

import refuel.json.JsonVal

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
  def deserialize(bf: JsonVal): T
}
