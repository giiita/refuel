package refuel.json

import refuel.json.codecs.{CodecContextType, CodecContextTypeExtension, Read, Write}
import refuel.json.conversion.{JsonRowEntry, JsonStructureEntry}
import refuel.json.spec.JsonKeyStructure

trait ImplicitCodecDefSupport {

  implicit def ImplicitRead[T](json: JsonVal)(implicit read: Read[T]): T = read.deserialize(json)
  implicit def ImplicitWrite[T](t: T)(implicit write: Write[T]): JsonVal = write.serialize(t)
  implicit def Imp[T, K[T] <: CodecContextType](codec: K[T]): CodecContextTypeExtension[T, K] = new CodecContextTypeExtension(codec)

  implicit def JsonRowToEntry(v: String): JsonRowEntry
  implicit def JsonTreeToEntry(x: JsonVal): JsonRowEntry = {
    new JsonRowEntry {
      def json: JsonVal = x
    }
  }
  implicit def EntryToJson[T](t: T): JsonStructureEntry[T] = new JsonStructureEntry(t)
  implicit def ToJsonKeySpec(v: String): JsonKeySpec = JsonKeyStructure(v)
}
