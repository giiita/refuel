package refuel.json.conversion

import refuel.json.JsonVal
import refuel.json.codecs.Write
import refuel.json.exception.InvalidDeserializationException

import scala.util.{Failure, Try}

/**
  * Serialize any object to Json syntax tree.
  * In this state, it is not JsonRawData, but it becomes JsonRawData by [[JsonVal.encode]].
  * A function that takes an implicit codec, but in many cases it will require explicit assignment.
  * {{{
  *   ???.toJson(CaseClassCodec.from[XXX])
  * }}}
  *
  * @param t Any object
  * @tparam T Any object type
  */
class JsonStructureEntry[T](t: T) {

  /** Use the `toJsonString` to perform JSON Serialize. At this time, the output JSON format string is always JSON ENCODE.
    * For example, if a JSON string contains a new line, the output is always the string "\\n".
    *
    * The toString operation will also output a JSON-style string, but it will not be JSON ENCODE.
    * For example, if you do a Json.str("foo\n\"bar"), you will get `"foo foo\\\\n\"bar"` by toJString, but you will get `"foo\n\"bar"` bar by toString.
    *
    * Also, as[String] returns the same result as toString.
    *
    * @param ct
    * @tparam X
    * @return
    **/
  def writeAsString[X >: T](implicit ct: Write[X]): Try[String] = {
    writeAs[X].map(_.toString)
  }

  def writeAs[X >: T](implicit ct: Write[X]): Try[JsonVal] = Try {
    ct.serialize(t)
  }.recoverWith {
    case x =>
      Failure(InvalidDeserializationException(s"Failed to serialize ${t.getClass} to Json.", x))
  }
}
