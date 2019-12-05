package refuel.internal.json

import refuel.internal.PropertyDebugModeEnabler
import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.error.DeserializeFailed
import refuel.json.{Codec, Json}

import scala.reflect.macros.blackbox

class ConstructCodecFactory(override val c: blackbox.Context) extends CaseCodecFactory(c) with PropertyDebugModeEnabler {

  import c.universe._

  private[this] final val JsonPkg = q"refuel.json"
  private[this] final val Codecs = q"refuel.json.codecs"
  private[this] final val JsonEntryPkg = q"refuel.json.entry"

  def fromConst1[A: c.WeakTypeTag, Z: c.WeakTypeTag](n1: c.Expr[JsKeyLitOps])
                                                    (apl: c.Expr[A => Z])
                                                    (upl: c.Expr[Z => Option[A]]): c.Expr[Codec[Z]] = {
    val describer: c.Expr[Json => Json] = reify { bf =>
      n1.splice.rec(bf)
    }
    val scriber: c.Expr[Json => Json] = c.Expr[Json => Json](
      q"""{ a =>
                   $JsonEntryPkg.JsObject()
                     .++($JsonEntryPkg.JsString($n1))
                     .++(a)
               }: ($JsonPkg.Json => $JsonPkg.Json)
          """)
    c.Expr[Codec[Z]] {
      q"""
           new $Codecs.JoinableCodec.T1($describer)($scriber)($apl)($upl)(${recall(weakTypeOf[A])})
         """
    }
  }

  def fromConst2[A: c.WeakTypeTag, B: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B) => Z])
  (upl: c.Expr[Z => Option[(A, B)]]): c.Expr[Codec[Z]] = {
    val describer: c.Expr[Json => (Json, Json)] = reify { bf =>
      n1.splice.rec(bf) -> n2.splice.rec(bf)
    }
    val scriber: c.Expr[(Json, Json) => Json] = c.Expr[(Json, Json) => Json](
      q"""({
               case (a, b) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
             }: ($JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """)
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T2($describer)($scriber)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}
         )
       """
    }
  }

  def fromConst3[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C) => Z])
  (upl: c.Expr[Z => Option[(A, B, C)]]): c.Expr[Codec[Z]] = {
    val describer: c.Expr[Json => (Json, Json, Json)] = reify { bf =>
      (n1.splice.rec(bf), n2.splice.rec(bf), n3.splice.rec(bf))
    }
    val scriber: c.Expr[(Json, Json, Json) => Json] = c.Expr[(Json, Json, Json) => Json](
      q"""({
               case (a, b, c) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """)
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T3($describer)($scriber)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}
         )
       """
    }
  }


  def fromConst4[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D)]]): c.Expr[Codec[Z]] = {
    val describer: c.Expr[Json => (Json, Json, Json, Json)] = reify { bf =>
      (n1.splice.rec(bf), n2.splice.rec(bf), n3.splice.rec(bf), n4.splice.rec(bf))
    }
    val scriber: c.Expr[(Json, Json, Json, Json) => Json] = c.Expr[(Json, Json, Json, Json) => Json](
      q"""({
               case (a, b, c, d) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """)
    c.Expr[Codec[Z]] {
      q"""
         new $Codecs.JoinableCodec.T4($describer)($scriber)($apl)($upl)(
           ${recall(weakTypeOf[A])}, ${recall(weakTypeOf[B])}, ${recall(weakTypeOf[C])}, ${recall(weakTypeOf[D])}
         )
       """
    }
  }

  def fromConst5[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .asTuple5[A, B, C, D, E]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5)
          )
        }
      }
    }
  }

  def fromConst6[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .asTuple6[A, B, C, D, E, F]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6)
          )
        }
      }
    }
  }

  def fromConst7[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .asTuple7[A, B, C, D, E, F, G]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7)
          )
        }
      }
    }
  }

  def fromConst8[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .asTuple8[A, B, C, D, E, F, G, H]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8)
          )
        }
      }
    }
  }

  def fromConst9[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .asTuple9[A, B, C, D, E, F, G, H, I]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9)
          )
        }
      }
    }
  }

  def fromConst10[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .asTuple10[A, B, C, D, E, F, G, H, I, J]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10)
          )
        }
      }
    }
  }

  def fromConst11[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .asTuple11[A, B, C, D, E, F, G, H, I, J, K]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11)
          )
        }
      }
    }
  }

  def fromConst12[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .asTuple12[A, B, C, D, E, F, G, H, I, J, K, L]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12)
          )
        }
      }
    }
  }

  def fromConst13[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .asTuple13[A, B, C, D, E, F, G, H, I, J, K, L, M]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13)
          )
        }
      }
    }
  }

  def fromConst14[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .asTuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14)
          )
        }
      }
    }
  }

  def fromConst15[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .asTuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15)
          )
        }
      }
    }
  }

  def fromConst16[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .asTuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16)
          )
        }
      }
    }
  }

  def fromConst17[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .and(recall(weakTypeOf[Q]).splice.deserialize(n17.splice.rec(bf)))
            .asTuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($JsonEntryPkg.JsString($n17))
                   .++(q)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16),
            recall(weakTypeOf[Q]).splice.serialize(z._17)
          )
        }
      }
    }
  }

  def fromConst18[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .and(recall(weakTypeOf[Q]).splice.deserialize(n17.splice.rec(bf)))
            .and(recall(weakTypeOf[R]).splice.deserialize(n18.splice.rec(bf)))
            .asTuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($JsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($JsonEntryPkg.JsString($n18))
                   .++(r)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16),
            recall(weakTypeOf[Q]).splice.serialize(z._17),
            recall(weakTypeOf[R]).splice.serialize(z._18)
          )
        }
      }
    }
  }

  def fromConst19[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .and(recall(weakTypeOf[Q]).splice.deserialize(n17.splice.rec(bf)))
            .and(recall(weakTypeOf[R]).splice.deserialize(n18.splice.rec(bf)))
            .and(recall(weakTypeOf[S]).splice.deserialize(n19.splice.rec(bf)))
            .asTuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($JsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($JsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($JsonEntryPkg.JsString($n19))
                   .++(s)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16),
            recall(weakTypeOf[Q]).splice.serialize(z._17),
            recall(weakTypeOf[R]).splice.serialize(z._18),
            recall(weakTypeOf[S]).splice.serialize(z._19)
          )
        }
      }
    }
  }

  def fromConst20[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps], n20: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .and(recall(weakTypeOf[Q]).splice.deserialize(n17.splice.rec(bf)))
            .and(recall(weakTypeOf[R]).splice.deserialize(n18.splice.rec(bf)))
            .and(recall(weakTypeOf[S]).splice.deserialize(n19.splice.rec(bf)))
            .and(recall(weakTypeOf[T]).splice.deserialize(n20.splice.rec(bf)))
            .asTuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, _t) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($JsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($JsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($JsonEntryPkg.JsString($n19))
                   .++(s)
                   .++($JsonEntryPkg.JsString($n20))
                   .++(_t)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16),
            recall(weakTypeOf[Q]).splice.serialize(z._17),
            recall(weakTypeOf[R]).splice.serialize(z._18),
            recall(weakTypeOf[S]).splice.serialize(z._19),
            recall(weakTypeOf[T]).splice.serialize(z._20)
          )
        }
      }
    }
  }

  def fromConst21[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, _U: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps], n20: c.Expr[JsKeyLitOps], n21: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .and(recall(weakTypeOf[Q]).splice.deserialize(n17.splice.rec(bf)))
            .and(recall(weakTypeOf[R]).splice.deserialize(n18.splice.rec(bf)))
            .and(recall(weakTypeOf[S]).splice.deserialize(n19.splice.rec(bf)))
            .and(recall(weakTypeOf[T]).splice.deserialize(n20.splice.rec(bf)))
            .and(recall(weakTypeOf[_U]).splice.deserialize(n21.splice.rec(bf)))
            .asTuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, _t, _u) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($JsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($JsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($JsonEntryPkg.JsString($n19))
                   .++(s)
                   .++($JsonEntryPkg.JsString($n20))
                   .++(_t)
                   .++($JsonEntryPkg.JsString($n21))
                   .++(_u)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16),
            recall(weakTypeOf[Q]).splice.serialize(z._17),
            recall(weakTypeOf[R]).splice.serialize(z._18),
            recall(weakTypeOf[S]).splice.serialize(z._19),
            recall(weakTypeOf[T]).splice.serialize(z._20),
            recall(weakTypeOf[_U]).splice.serialize(z._21)
          )
        }
      }
    }
  }

  def fromConst22[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, _U: c.WeakTypeTag, V: c.WeakTypeTag, Z]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps], n5: c.Expr[JsKeyLitOps], n6: c.Expr[JsKeyLitOps], n7: c.Expr[JsKeyLitOps], n8: c.Expr[JsKeyLitOps], n9: c.Expr[JsKeyLitOps], n10: c.Expr[JsKeyLitOps], n11: c.Expr[JsKeyLitOps], n12: c.Expr[JsKeyLitOps], n13: c.Expr[JsKeyLitOps], n14: c.Expr[JsKeyLitOps], n15: c.Expr[JsKeyLitOps], n16: c.Expr[JsKeyLitOps], n17: c.Expr[JsKeyLitOps], n18: c.Expr[JsKeyLitOps], n19: c.Expr[JsKeyLitOps], n20: c.Expr[JsKeyLitOps], n21: c.Expr[JsKeyLitOps], n22: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V)]])(implicit zt: c.WeakTypeTag[Z]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .and(recall(weakTypeOf[E]).splice.deserialize(n5.splice.rec(bf)))
            .and(recall(weakTypeOf[F]).splice.deserialize(n6.splice.rec(bf)))
            .and(recall(weakTypeOf[G]).splice.deserialize(n7.splice.rec(bf)))
            .and(recall(weakTypeOf[H]).splice.deserialize(n8.splice.rec(bf)))
            .and(recall(weakTypeOf[I]).splice.deserialize(n9.splice.rec(bf)))
            .and(recall(weakTypeOf[J]).splice.deserialize(n10.splice.rec(bf)))
            .and(recall(weakTypeOf[K]).splice.deserialize(n11.splice.rec(bf)))
            .and(recall(weakTypeOf[L]).splice.deserialize(n12.splice.rec(bf)))
            .and(recall(weakTypeOf[M]).splice.deserialize(n13.splice.rec(bf)))
            .and(recall(weakTypeOf[N]).splice.deserialize(n14.splice.rec(bf)))
            .and(recall(weakTypeOf[O]).splice.deserialize(n15.splice.rec(bf)))
            .and(recall(weakTypeOf[P]).splice.deserialize(n16.splice.rec(bf)))
            .and(recall(weakTypeOf[Q]).splice.deserialize(n17.splice.rec(bf)))
            .and(recall(weakTypeOf[R]).splice.deserialize(n18.splice.rec(bf)))
            .and(recall(weakTypeOf[S]).splice.deserialize(n19.splice.rec(bf)))
            .and(recall(weakTypeOf[T]).splice.deserialize(n20.splice.rec(bf)))
            .and(recall(weakTypeOf[_U]).splice.deserialize(n21.splice.rec(bf)))
            .and(recall(weakTypeOf[V]).splice.deserialize(n22.splice.rec(bf)))
            .asTuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, _U, V]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, _t, _u, v) =>
                 $JsonEntryPkg.JsObject()
                   .++($JsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($JsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($JsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($JsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($JsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($JsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($JsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($JsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($JsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($JsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($JsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($JsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($JsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($JsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($JsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($JsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($JsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($JsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($JsonEntryPkg.JsString($n19))
                   .++(s)
                   .++($JsonEntryPkg.JsString($n20))
                   .++(_t)
                   .++($JsonEntryPkg.JsString($n21))
                   .++(_u)
                   .++($JsonEntryPkg.JsString($n22))
                   .++(v)
             }: ($JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json, $JsonPkg.Json) => $JsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4),
            recall(weakTypeOf[E]).splice.serialize(z._5),
            recall(weakTypeOf[F]).splice.serialize(z._6),
            recall(weakTypeOf[G]).splice.serialize(z._7),
            recall(weakTypeOf[H]).splice.serialize(z._8),
            recall(weakTypeOf[I]).splice.serialize(z._9),
            recall(weakTypeOf[J]).splice.serialize(z._10),
            recall(weakTypeOf[K]).splice.serialize(z._11),
            recall(weakTypeOf[L]).splice.serialize(z._12),
            recall(weakTypeOf[M]).splice.serialize(z._13),
            recall(weakTypeOf[N]).splice.serialize(z._14),
            recall(weakTypeOf[O]).splice.serialize(z._15),
            recall(weakTypeOf[P]).splice.serialize(z._16),
            recall(weakTypeOf[Q]).splice.serialize(z._17),
            recall(weakTypeOf[R]).splice.serialize(z._18),
            recall(weakTypeOf[S]).splice.serialize(z._19),
            recall(weakTypeOf[T]).splice.serialize(z._20),
            recall(weakTypeOf[_U]).splice.serialize(z._21),
            recall(weakTypeOf[V]).splice.serialize(z._22)
          )
        }
      }
    }
  }
}
