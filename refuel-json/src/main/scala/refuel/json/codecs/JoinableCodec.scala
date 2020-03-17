package refuel.json.codecs

import refuel.internal.json.DeserializeResult
import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.entry.JsNull
import refuel.json.error.{DeserializeFailPropagation, DeserializeFailed}
import refuel.json.{Codec, JsonVal}

object JoinableCodec {

  abstract class T1[A, Z](apl: A => Z)
                         (upl: Z => Option[A])
                         (implicit n1: Codec[A]) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t) match {
      case Some(a) => keyLiteralRef.additionalKeyRef(Seq(n1.serialize(a)))
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      n1.deserialize(keyLiteralRef.rec(bf).head) match {
        case Right(x) => Right(apl(x))
        case Left(e) => Left(
          DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
        )
      }
    }
  }

  abstract class T2[A, B, Z](apl: (A, B) => Z)
                            (upl: Z => Option[(A, B)])
                            (implicit n1: Codec[A], n2: Codec[B]) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t) match {
      case Some((a, b)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .asTuple2 match {
          case Right(x) => Right(apl.tupled(x))
          case Left(e) => Left(
            DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
          )
        }
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef
    }
  }

  abstract class T3[A, B, C, Z](apl: (A, B, C) => Z)
                               (upl: Z => Option[(A, B, C)])
                               (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C]) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t) match {
      case Some((a, b, c)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .asTuple3 match {
          case Right(x) => Right(apl.tupled(x))
          case Left(e) => Left(
            DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
          )
        }
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef
    }
  }

  abstract class T4[A, B, C, D, Z](apl: (A, B, C, D) => Z)
                                  (upl: Z => Option[(A, B, C, D)])
                                  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D]) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t) match {
      case Some((a, b, c, d)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .asTuple4 match {
          case Right(x) => Right(apl.tupled(x))
          case Left(e) => Left(
            DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
          )
        }
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef
    }
  }

  abstract class T5[A, B, C, D, E, Z]
  (apl: (A, B, C, D, E) => Z)
  (upl: Z => Option[(A, B, C, D, E)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .asTuple5
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef
    }
  }

  abstract class T6[A, B, C, D, E, F, Z]
  (apl: (A, B, C, D, E, F) => Z)
  (upl: Z => Option[(A, B, C, D, E, F)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .asTuple6
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef
    }
  }

  abstract class T7[A, B, C, D, E, F, G, Z]
  (apl: (A, B, C, D, E, F, G) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .asTuple7
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef
    }
  }

  abstract class T8[A, B, C, D, E, F, G, H, Z]
  (apl: (A, B, C, D, E, F, G, H) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .asTuple8
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef
    }
  }

  abstract class T9[A, B, C, D, E, F, G, H, I, Z]
  (apl: (A, B, C, D, E, F, G, H, I) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .asTuple9
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef
    }
  }

  abstract class T10[A, B, C, D, E, F, G, H, I, J, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .asTuple10
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef
    }
  }

  abstract class T11[A, B, C, D, E, F, G, H, I, J, K, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .asTuple11
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef
    }
  }

  abstract class T12[A, B, C, D, E, F, G, H, I, J, K, L, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .asTuple12
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef
    }
  }

  abstract class T13[A, B, C, D, E, F, G, H, I, J, K, L, M, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .asTuple13
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef
    }
  }

  abstract class T14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n)
        ))
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .asTuple14
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef
    }
  }

  abstract class T15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .asTuple15
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef
    }
  }

  abstract class T16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .asTuple16
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef ++
        n16.keyLiteralRef
    }
  }

  abstract class T17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p),
          n17.serialize(q)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .and(n17.deserialize(a(16)))
          .asTuple17
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef ++
        n16.keyLiteralRef ++
        n17.keyLiteralRef
    }
  }

  abstract class T18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p),
          n17.serialize(q),
          n18.serialize(r)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .and(n17.deserialize(a(16)))
          .and(n18.deserialize(a(17)))
          .asTuple18
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef ++
        n16.keyLiteralRef ++
        n17.keyLiteralRef ++
        n18.keyLiteralRef
    }
  }

  abstract class T19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p),
          n17.serialize(q),
          n18.serialize(r),
          n19.serialize(s)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .and(n17.deserialize(a(16)))
          .and(n18.deserialize(a(17)))
          .and(n19.deserialize(a(18)))
          .asTuple19
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef ++
        n16.keyLiteralRef ++
        n17.keyLiteralRef ++
        n18.keyLiteralRef ++
        n19.keyLiteralRef
    }
  }

  abstract class T20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S], n20: Codec[T]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p),
          n17.serialize(q),
          n18.serialize(r),
          n19.serialize(s),
          n20.serialize(t)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .and(n17.deserialize(a(16)))
          .and(n18.deserialize(a(17)))
          .and(n19.deserialize(a(18)))
          .and(n20.deserialize(a(19)))
          .asTuple20
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef ++
        n16.keyLiteralRef ++
        n17.keyLiteralRef ++
        n18.keyLiteralRef ++
        n19.keyLiteralRef ++
        n20.keyLiteralRef
    }
  }

  abstract class T21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S], n20: Codec[T], n21: Codec[U]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p),
          n17.serialize(q),
          n18.serialize(r),
          n19.serialize(s),
          n20.serialize(t),
          n21.serialize(u)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .and(n17.deserialize(a(16)))
          .and(n18.deserialize(a(17)))
          .and(n19.deserialize(a(18)))
          .and(n20.deserialize(a(19)))
          .and(n21.deserialize(a(20)))
          .asTuple21
          .right.map(apl.tupled)
      }
    }

    override val keyLiteralRef: JsKeyLitOps = {
      n1.keyLiteralRef ++
        n2.keyLiteralRef ++
        n3.keyLiteralRef ++
        n4.keyLiteralRef ++
        n5.keyLiteralRef ++
        n6.keyLiteralRef ++
        n7.keyLiteralRef ++
        n8.keyLiteralRef ++
        n9.keyLiteralRef ++
        n10.keyLiteralRef ++
        n11.keyLiteralRef ++
        n12.keyLiteralRef ++
        n13.keyLiteralRef ++
        n14.keyLiteralRef ++
        n15.keyLiteralRef ++
        n16.keyLiteralRef ++
        n17.keyLiteralRef ++
        n18.keyLiteralRef ++
        n19.keyLiteralRef ++
        n20.keyLiteralRef ++
        n21.keyLiteralRef
    }
  }

  abstract class T22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z]
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S], n20: Codec[T], n21: Codec[U], n22: Codec[V]) extends Codec[Z] {
    override def serialize(_t: Z): JsonVal = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)) => keyLiteralRef.additionalKeyRef(
        Seq(
          n1.serialize(a),
          n2.serialize(b),
          n3.serialize(c),
          n4.serialize(d),
          n5.serialize(e),
          n6.serialize(f),
          n7.serialize(g),
          n8.serialize(h),
          n9.serialize(i),
          n10.serialize(j),
          n11.serialize(k),
          n12.serialize(l),
          n13.serialize(m),
          n14.serialize(n),
          n15.serialize(o),
          n16.serialize(p),
          n17.serialize(q),
          n18.serialize(r),
          n19.serialize(s),
          n20.serialize(t),
          n21.serialize(u),
          n22.serialize(v)
        )
      )
      case None => JsNull
    }

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Z] = {
      keyLiteralRef.rec(bf) match {
        case a => DeserializeResult(n1.deserialize(a.head))
          .and(n2.deserialize(a(1)))
          .and(n3.deserialize(a(2)))
          .and(n4.deserialize(a(3)))
          .and(n5.deserialize(a(4)))
          .and(n6.deserialize(a(5)))
          .and(n7.deserialize(a(6)))
          .and(n8.deserialize(a(7)))
          .and(n9.deserialize(a(8)))
          .and(n10.deserialize(a(9)))
          .and(n11.deserialize(a(10)))
          .and(n12.deserialize(a(11)))
          .and(n13.deserialize(a(12)))
          .and(n14.deserialize(a(13)))
          .and(n15.deserialize(a(14)))
          .and(n16.deserialize(a(15)))
          .and(n17.deserialize(a(16)))
          .and(n18.deserialize(a(17)))
          .and(n19.deserialize(a(18)))
          .and(n20.deserialize(a(19)))
          .and(n21.deserialize(a(20)))
          .and(n22.deserialize(a(21)))
          .asTuple22
          .right.map(apl.tupled)
      }
    }
  }

}
