package refuel.json.codecs

import refuel.internal.json.DeserializeResult
import refuel.json.entry.JsNull
import refuel.json.error.{DeserializeFailPropagation, DeserializeFailed}
import refuel.json.{Codec, Json}

object JoinableCodec {

  class T1[A, Z](describer: Json => Json)
                (scriber: Json => Json)
                (apl: A => Z)
                (upl: Z => Option[A])
                (implicit n1: Codec[A]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some(a) => scriber.apply(n1.serialize(a))
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      n1.deserialize(describer(bf)) match {
        case Right(x) => Right(apl(x))
        case Left(e) => Left(
          DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
        )
      }
    }
  }

  class T2[A, B, Z](describer: Json => (Json, Json))
                   (scriber: (Json, Json) => Json)
                   (apl: (A, B) => Z)
                   (upl: Z => Option[(A, B)])
                   (implicit n1: Codec[A], n2: Codec[B]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some((a, b)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .asTuple2 match {
          case Right(x) => Right(apl.tupled(x))
          case Left(e) => Left(
            DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
          )
        }
      }
    }
  }

  class T3[A, B, C, Z](describer: Json => (Json, Json, Json))
                      (scriber: (Json, Json, Json) => Json)
                      (apl: (A, B, C) => Z)
                      (upl: Z => Option[(A, B, C)])
                      (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some((a, b, c)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .asTuple3 match {
          case Right(x) => Right(apl.tupled(x))
          case Left(e) => Left(
            DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
          )
        }
      }
    }
  }

  class T4[A, B, C, D, Z](describer: Json => (Json, Json, Json, Json))
                         (scriber: (Json, Json, Json, Json) => Json)
                         (apl: (A, B, C, D) => Z)
                         (upl: Z => Option[(A, B, C, D)])
                         (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D]) extends Codec[Z] {
    override def serialize(t: Z): Json = upl(t) match {
      case Some((a, b, c, d)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c),
        n4.serialize(d)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .asTuple4 match {
          case Right(x) => Right(apl.tupled(x))
          case Left(e) => Left(
            DeserializeFailPropagation(s"Internal structure analysis raised an exception at $bf", e)
          )
        }
      }
    }
  }

  class T5[A, B, C, D, E, Z]
  (describer: Json => (Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E) => Z)
  (upl: Z => Option[(A, B, C, D, E)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c),
        n4.serialize(d),
        n5.serialize(e)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .asTuple5
          .right.map(apl.tupled)
      }
    }
  }

  class T6[A, B, C, D, E, F, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F) => Z)
  (upl: Z => Option[(A, B, C, D, E, F)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c),
        n4.serialize(d),
        n5.serialize(e),
        n6.serialize(f)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .asTuple6
          .right.map(apl.tupled)
      }
    }
  }

  class T7[A, B, C, D, E, F, G, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c),
        n4.serialize(d),
        n5.serialize(e),
        n6.serialize(f),
        n7.serialize(g)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .asTuple7
          .right.map(apl.tupled)
      }
    }
  }

  class T8[A, B, C, D, E, F, G, H, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h)) => scriber.apply(
        n1.serialize(a),
        n2.serialize(b),
        n3.serialize(c),
        n4.serialize(d),
        n5.serialize(e),
        n6.serialize(f),
        n7.serialize(g),
        n8.serialize(h)
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .asTuple8
          .right.map(apl.tupled)
      }
    }
  }

  class T9[A, B, C, D, E, F, G, H, I, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .asTuple9
          .right.map(apl.tupled)
      }
    }
  }

  class T10[A, B, C, D, E, F, G, H, I, J, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .asTuple10
          .right.map(apl.tupled)
      }
    }
  }

  class T11[A, B, C, D, E, F, G, H, I, J, K, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .asTuple11
          .right.map(apl.tupled)
      }
    }
  }

  class T12[A, B, C, D, E, F, G, H, I, J, K, L, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .asTuple12
          .right.map(apl.tupled)
      }
    }
  }

  class T13[A, B, C, D, E, F, G, H, I, J, K, L, M, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .asTuple13
          .right.map(apl.tupled)
      }
    }
  }

  class T14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n)) => scriber.apply(
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
      )
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .asTuple14
          .right.map(apl.tupled)
      }
    }
  }

  class T15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .asTuple15
          .right.map(apl.tupled)
      }
    }
  }

  class T16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .asTuple16
          .right.map(apl.tupled)
      }
    }
  }

  class T17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .and(n17.deserialize(q))
          .asTuple17
          .right.map(apl.tupled)
      }
    }
  }

  class T18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .and(n17.deserialize(q))
          .and(n18.deserialize(r))
          .asTuple18
          .right.map(apl.tupled)
      }
    }
  }

  class T19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .and(n17.deserialize(q))
          .and(n18.deserialize(r))
          .and(n19.deserialize(s))
          .asTuple19
          .right.map(apl.tupled)
      }
    }
  }

  class T20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S], n20: Codec[T]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .and(n17.deserialize(q))
          .and(n18.deserialize(r))
          .and(n19.deserialize(s))
          .and(n20.deserialize(t))
          .asTuple20
          .right.map(apl.tupled)
      }
    }
  }

  class T21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S], n20: Codec[T], n21: Codec[U]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .and(n17.deserialize(q))
          .and(n18.deserialize(r))
          .and(n19.deserialize(s))
          .and(n20.deserialize(t))
          .and(n21.deserialize(u))
          .asTuple21
          .right.map(apl.tupled)
      }
    }
  }

  class T22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z]
  (describer: Json => (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json))
  (scriber: (Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json, Json) => Json)
  (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z)
  (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])
  (implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E], n6: Codec[F], n7: Codec[G], n8: Codec[H], n9: Codec[I], n10: Codec[J], n11: Codec[K], n12: Codec[L], n13: Codec[M], n14: Codec[N], n15: Codec[O], n16: Codec[P], n17: Codec[Q], n18: Codec[R], n19: Codec[S], n20: Codec[T], n21: Codec[U], n22: Codec[V]) extends Codec[Z] {
    override def serialize(_t: Z): Json = upl(_t) match {
      case Some((a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)) => scriber.apply(
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
      case None => JsNull
    }

    override def deserialize(bf: Json): Either[DeserializeFailed, Z] = {
      describer(bf) match {
        case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => DeserializeResult(n1.deserialize(a))
          .and(n2.deserialize(b))
          .and(n3.deserialize(c))
          .and(n4.deserialize(d))
          .and(n5.deserialize(e))
          .and(n6.deserialize(f))
          .and(n7.deserialize(g))
          .and(n8.deserialize(h))
          .and(n9.deserialize(i))
          .and(n10.deserialize(j))
          .and(n11.deserialize(k))
          .and(n12.deserialize(l))
          .and(n13.deserialize(m))
          .and(n14.deserialize(n))
          .and(n15.deserialize(o))
          .and(n16.deserialize(p))
          .and(n17.deserialize(q))
          .and(n18.deserialize(r))
          .and(n19.deserialize(s))
          .and(n20.deserialize(t))
          .and(n21.deserialize(u))
          .and(n22.deserialize(v))
          .asTuple22
          .right.map(apl.tupled)
      }
    }
  }

}
