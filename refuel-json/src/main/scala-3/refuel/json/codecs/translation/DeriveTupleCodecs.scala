package refuel.json.codecs.translation

import refuel.json.JsonVal.JsObject
import refuel.json.codecs.{Codec, CodecTypeProjection}
import refuel.json.compiletime.DeriveCodecs
import refuel.json.{Json, JsonVal}
import xsbti.api.EmptyType

import scala.annotation.tailrec
import scala.deriving.Mirror
import scala.compiletime.{erasedValue, error, summonFrom}

private[refuel] object DeriveTupleCodecs {
  private inline def inferCodec[A, C[_]: CodecTypeProjection]: C[A] = {
    summonFrom[C[A]] {
      case x: C[A] => x
      case _ => error("It is not possible to derive a class that does not support Product. Use Derive[T] instead, or define an implicit scope for the Codec of a non-Product class that depends on it internally.")
    }
  }

  inline def foldSummon[T <: Tuple, C[_]: CodecTypeProjection]: Tuple.Map[T, C] =
    inline erasedValue[T] match {
      case _: EmptyTuple =>
        EmptyTuple
      case _: (t *: ts) =>
        inferCodec[t, C] *: foldSummon[ts, C]
    }
}
