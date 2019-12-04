package refuel.json

import refuel.json.error.DeserializeFailed

trait Json extends Serializable {
  def toString: String
  def unquote: String = toString

  def decode: Json = this

  def ++(js: Json): Json

  def isIndependent: Boolean = false

  def to[T](implicit c: Codec[T]): Either[DeserializeFailed, T] =
    c.deserialize(this)

  def named(key: String): Json
}
