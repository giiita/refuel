package refuel.json

import refuel.json.codecs.{CodecContextType, CodecContextTypeExtension, Read, Write}
import refuel.json.conversion.{JsonRowEntry, JsonStructureEntry}
import refuel.json.spec.JsonKeyStructure

trait ImplicitCodecDefSupport {

  given ImplicitRead[T](using read: Read[T]): scala.Conversion[JsonVal, T] = read.deserialize
  given ImplicitWrite[T](using write: Write[T]): scala.Conversion[T, JsonVal] = write.serialize
  given Imp[T, K[T] <: CodecContextType]: scala.Conversion[K[T], CodecContextTypeExtension[T, K]] = new CodecContextTypeExtension(_)

  given JsonRowToEntry: scala.Conversion[String, JsonRowEntry]
  given JsonTreeToEntry: scala.Conversion[JsonVal, JsonRowEntry] = { x =>
    new JsonRowEntry {
      def json: JsonVal = x
    }
  }
  given EntryToJson[T]: scala.Conversion[T, JsonStructureEntry[T]] = new JsonStructureEntry(_)
  given ToJsonKeySpec: scala.Conversion[String, JsonKeySpec] = JsonKeyStructure(_)
}
