package refuel.internal.json

import refuel.internal.PropertyDebugModeEnabler
import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.{Codec, Json}
import refuel.json.error.DeserializeFailed

import scala.reflect.macros.blackbox

class ConstructCodecFactory(override val c: blackbox.Context) extends CaseCodecFactory(c) with  PropertyDebugModeEnabler {

  import c.universe._

  private[this] val jsonPkg = q"refuel.json"
  private[this] val jsonEntryPkg = q"refuel.json.entry"

  def fromConst1[A: c.WeakTypeTag, Z: c.WeakTypeTag](n1: c.Expr[JsKeyLitOps])
                                                    (apl: c.Expr[A => Z])
                                                    (upl: c.Expr[Z => Option[A]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          recall(weakTypeOf[A]).splice
            .deserialize(n1.splice.rec(bf)) match {
            case Right(x) => Right(apl.splice.apply(x))
            case Left(e) => Left(e)
          }
        }

        override def serialize(t: Z): Json = {
          c.Expr[Json => Json](
            q"""
             $jsonEntryPkg.JsObject()
               .++($jsonEntryPkg.JsString($n1))
               .++(_)
           """).splice.apply(recall(weakTypeOf[A]).splice.serialize(upl.splice.apply(t).get))
        }
      }
    }
  }

  def fromConst2[A: c.WeakTypeTag, B: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B) => Z])
  (upl: c.Expr[Z => Option[(A, B)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .asTuple2[A, B]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json) => Json](
            q"""({
               case (a, b) =>
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
             }: ($jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2)
          )
        }
      }
    }
  }

  def fromConst3[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C) => Z])
  (upl: c.Expr[Z => Option[(A, B, C)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .asTuple3[A, B, C]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json) => Json](
            q"""({
               case (a, b, c) =>
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3)
          )
        }
      }
    }
  }

  def fromConst4[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, Z: c.WeakTypeTag]
  (n1: c.Expr[JsKeyLitOps], n2: c.Expr[JsKeyLitOps], n3: c.Expr[JsKeyLitOps], n4: c.Expr[JsKeyLitOps])
  (apl: c.Expr[(A, B, C, D) => Z])
  (upl: c.Expr[Z => Option[(A, B, C, D)]]): c.Expr[Codec[Z]] = {
    reify {
      new Codec[Z] {
        override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
          DeserializeResult(recall(weakTypeOf[A]).splice.deserialize(n1.splice.rec(bf)))
            .and(recall(weakTypeOf[B]).splice.deserialize(n2.splice.rec(bf)))
            .and(recall(weakTypeOf[C]).splice.deserialize(n3.splice.rec(bf)))
            .and(recall(weakTypeOf[D]).splice.deserialize(n4.splice.rec(bf)))
            .asTuple4[A, B, C, D]
            .right.map(apl.splice.tupled.apply)
        }

        override def serialize(t: Z): Json = {
          val z = upl.splice.apply(t).get
          c.Expr[(Json, Json, Json, Json) => Json](
            q"""({
               case (a, b, c, d) =>
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
           """).splice.apply(
            recall(weakTypeOf[A]).splice.serialize(z._1),
            recall(weakTypeOf[B]).splice.serialize(z._2),
            recall(weakTypeOf[C]).splice.serialize(z._3),
            recall(weakTypeOf[D]).splice.serialize(z._4)
          )
        }
      }
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($jsonEntryPkg.JsString($n17))
                   .++(q)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($jsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($jsonEntryPkg.JsString($n18))
                   .++(r)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($jsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($jsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($jsonEntryPkg.JsString($n19))
                   .++(s)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($jsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($jsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($jsonEntryPkg.JsString($n19))
                   .++(s)
                   .++($jsonEntryPkg.JsString($n20))
                   .++(_t)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($jsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($jsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($jsonEntryPkg.JsString($n19))
                   .++(s)
                   .++($jsonEntryPkg.JsString($n20))
                   .++(_t)
                   .++($jsonEntryPkg.JsString($n21))
                   .++(_u)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
                 $jsonEntryPkg.JsObject()
                   .++($jsonEntryPkg.JsString($n1))
                   .++(a)
                   .++($jsonEntryPkg.JsString($n2))
                   .++(b)
                   .++($jsonEntryPkg.JsString($n3))
                   .++(c)
                   .++($jsonEntryPkg.JsString($n4))
                   .++(d)
                   .++($jsonEntryPkg.JsString($n5))
                   .++(e)
                   .++($jsonEntryPkg.JsString($n6))
                   .++(f)
                   .++($jsonEntryPkg.JsString($n7))
                   .++(g)
                   .++($jsonEntryPkg.JsString($n8))
                   .++(h)
                   .++($jsonEntryPkg.JsString($n9))
                   .++(i)
                   .++($jsonEntryPkg.JsString($n10))
                   .++(j)
                   .++($jsonEntryPkg.JsString($n11))
                   .++(k)
                   .++($jsonEntryPkg.JsString($n12))
                   .++(l)
                   .++($jsonEntryPkg.JsString($n13))
                   .++(m)
                   .++($jsonEntryPkg.JsString($n14))
                   .++(n)
                   .++($jsonEntryPkg.JsString($n15))
                   .++(o)
                   .++($jsonEntryPkg.JsString($n16))
                   .++(p)
                   .++($jsonEntryPkg.JsString($n17))
                   .++(q)
                   .++($jsonEntryPkg.JsString($n18))
                   .++(r)
                   .++($jsonEntryPkg.JsString($n19))
                   .++(s)
                   .++($jsonEntryPkg.JsString($n20))
                   .++(_t)
                   .++($jsonEntryPkg.JsString($n21))
                   .++(_u)
                   .++($jsonEntryPkg.JsString($n22))
                   .++(v)
             }: ($jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json, $jsonPkg.Json) => $jsonPkg.Json)
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
