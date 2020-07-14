package refuel.json.codecs

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.entry.{JsNull, JsObject}
import refuel.json.{Codec, JsonVal}

object JoinableCodec {

  class T0[A, Z](apl: A => Z)(upl: Z => Option[A])(implicit n1: Codec[A]) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) { x => n1.serialize(x) }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(bf)
      )
    }
  }

  class T1[A, Z](s1: JsonKeyRef)(apl: A => Z)(upl: Z => Option[A])(implicit n1: Codec[A]) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) { x =>
      JsObject.fromEntry(
        s1 ->> n1.serialize(x)
      )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf))
      )
    }
  }

  class T2[A, B, Z](s1: JsonKeyRef, s2: JsonKeyRef)(apl: (A, B) => Z)(upl: Z => Option[(A, B)])(
      implicit n1: Codec[A],
      n2: Codec[B]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf))
      )
    }
  }

  class T3[A, B, C, Z](s1: JsonKeyRef, s2: JsonKeyRef, s3: JsonKeyRef)(apl: (A, B, C) => Z)(upl: Z => Option[(A, B, C)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf))
      )
    }
  }

  class T4[A, B, C, D, Z](s1: JsonKeyRef, s2: JsonKeyRef, s3: JsonKeyRef, s4: JsonKeyRef)(apl: (A, B, C, D) => Z)(
      upl: Z => Option[(A, B, C, D)]
  )(implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D])
      extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf))
      )
    }
  }

  class T5[A, B, C, D, E, Z](s1: JsonKeyRef, s2: JsonKeyRef, s3: JsonKeyRef, s4: JsonKeyRef, s5: JsonKeyRef)(
      apl: (A, B, C, D, E) => Z
  )(upl: Z => Option[(A, B, C, D, E)])(implicit n1: Codec[A], n2: Codec[B], n3: Codec[C], n4: Codec[D], n5: Codec[E])
      extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf))
      )
    }
  }

  class T6[A, B, C, D, E, F, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef
  )(apl: (A, B, C, D, E, F) => Z)(upl: Z => Option[(A, B, C, D, E, F)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf))
      )
    }
  }

  class T7[A, B, C, D, E, F, G, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G) => Z)(upl: Z => Option[(A, B, C, D, E, F, G)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf))
      )
    }
  }

  class T8[A, B, C, D, E, F, G, H, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf))
      )
    }
  }

  class T9[A, B, C, D, E, F, G, H, I, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf))
      )
    }
  }

  class T10[A, B, C, D, E, F, G, H, I, J, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf))
      )
    }
  }

  class T11[A, B, C, D, E, F, G, H, I, J, K, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf))
      )
    }
  }

  class T12[A, B, C, D, E, F, G, H, I, J, K, L, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf))
      )
    }
  }

  class T13[A, B, C, D, E, F, G, H, I, J, K, L, M, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf))
      )
    }
  }

  class T14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf))
      )
    }
  }

  class T15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf))
      )
    }
  }

  class T16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf))
      )
    }
  }

  class T17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef,
      s17: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P],
      n17: Codec[Q]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p),
          s17 ->> n17.serialize(q)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf)),
        n17.deserialize(s17.dig(bf))
      )
    }
  }

  class T18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef,
      s17: JsonKeyRef,
      s18: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P],
      n17: Codec[Q],
      n18: Codec[R]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p),
          s17 ->> n17.serialize(q),
          s18 ->> n18.serialize(r)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf)),
        n17.deserialize(s17.dig(bf)),
        n18.deserialize(s18.dig(bf))
      )
    }
  }

  class T19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef,
      s17: JsonKeyRef,
      s18: JsonKeyRef,
      s19: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P],
      n17: Codec[Q],
      n18: Codec[R],
      n19: Codec[S]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p),
          s17 ->> n17.serialize(q),
          s18 ->> n18.serialize(r),
          s19 ->> n19.serialize(s)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf)),
        n17.deserialize(s17.dig(bf)),
        n18.deserialize(s18.dig(bf)),
        n19.deserialize(s19.dig(bf))
      )
    }
  }

  class T20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef,
      s17: JsonKeyRef,
      s18: JsonKeyRef,
      s19: JsonKeyRef,
      s20: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P],
      n17: Codec[Q],
      n18: Codec[R],
      n19: Codec[S],
      n20: Codec[T]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p),
          s17 ->> n17.serialize(q),
          s18 ->> n18.serialize(r),
          s19 ->> n19.serialize(s),
          s20 ->> n20.serialize(t)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf)),
        n17.deserialize(s17.dig(bf)),
        n18.deserialize(s18.dig(bf)),
        n19.deserialize(s19.dig(bf)),
        n20.deserialize(s20.dig(bf))
      )
    }
  }

  class T21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef,
      s17: JsonKeyRef,
      s18: JsonKeyRef,
      s19: JsonKeyRef,
      s20: JsonKeyRef,
      s21: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P],
      n17: Codec[Q],
      n18: Codec[R],
      n19: Codec[S],
      n20: Codec[T],
      n21: Codec[U]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p),
          s17 ->> n17.serialize(q),
          s18 ->> n18.serialize(r),
          s19 ->> n19.serialize(s),
          s20 ->> n20.serialize(t),
          s21 ->> n21.serialize(u)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf)),
        n17.deserialize(s17.dig(bf)),
        n18.deserialize(s18.dig(bf)),
        n19.deserialize(s19.dig(bf)),
        n20.deserialize(s20.dig(bf)),
        n21.deserialize(s21.dig(bf))
      )
    }
  }

  class T22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z](
      s1: JsonKeyRef,
      s2: JsonKeyRef,
      s3: JsonKeyRef,
      s4: JsonKeyRef,
      s5: JsonKeyRef,
      s6: JsonKeyRef,
      s7: JsonKeyRef,
      s8: JsonKeyRef,
      s9: JsonKeyRef,
      s10: JsonKeyRef,
      s11: JsonKeyRef,
      s12: JsonKeyRef,
      s13: JsonKeyRef,
      s14: JsonKeyRef,
      s15: JsonKeyRef,
      s16: JsonKeyRef,
      s17: JsonKeyRef,
      s18: JsonKeyRef,
      s19: JsonKeyRef,
      s20: JsonKeyRef,
      s21: JsonKeyRef,
      s22: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]
  )(
      implicit n1: Codec[A],
      n2: Codec[B],
      n3: Codec[C],
      n4: Codec[D],
      n5: Codec[E],
      n6: Codec[F],
      n7: Codec[G],
      n8: Codec[H],
      n9: Codec[I],
      n10: Codec[J],
      n11: Codec[K],
      n12: Codec[L],
      n13: Codec[M],
      n14: Codec[N],
      n15: Codec[O],
      n16: Codec[P],
      n17: Codec[Q],
      n18: Codec[R],
      n19: Codec[S],
      n20: Codec[T],
      n21: Codec[U],
      n22: Codec[V]
  ) extends Codec[Z] {
    override def serialize(t: Z): JsonVal = upl(t).fold[JsonVal](JsNull) {
      case (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) =>
        JsObject.fromEntry(
          s1 ->> n1.serialize(a),
          s2 ->> n2.serialize(b),
          s3 ->> n3.serialize(c),
          s4 ->> n4.serialize(d),
          s5 ->> n5.serialize(e),
          s6 ->> n6.serialize(f),
          s7 ->> n7.serialize(g),
          s8 ->> n8.serialize(h),
          s9 ->> n9.serialize(i),
          s10 ->> n10.serialize(j),
          s11 ->> n11.serialize(k),
          s12 ->> n12.serialize(l),
          s13 ->> n13.serialize(m),
          s14 ->> n14.serialize(n),
          s15 ->> n15.serialize(o),
          s16 ->> n16.serialize(p),
          s17 ->> n17.serialize(q),
          s18 ->> n18.serialize(r),
          s19 ->> n19.serialize(s),
          s20 ->> n20.serialize(t),
          s21 ->> n21.serialize(u),
          s22 ->> n22.serialize(v)
        )
    }

    override def deserialize(bf: JsonVal): Z = {
      apl(
        n1.deserialize(s1.dig(bf)),
        n2.deserialize(s2.dig(bf)),
        n3.deserialize(s3.dig(bf)),
        n4.deserialize(s4.dig(bf)),
        n5.deserialize(s5.dig(bf)),
        n6.deserialize(s6.dig(bf)),
        n7.deserialize(s7.dig(bf)),
        n8.deserialize(s8.dig(bf)),
        n9.deserialize(s9.dig(bf)),
        n10.deserialize(s10.dig(bf)),
        n11.deserialize(s11.dig(bf)),
        n12.deserialize(s12.dig(bf)),
        n13.deserialize(s13.dig(bf)),
        n14.deserialize(s14.dig(bf)),
        n15.deserialize(s15.dig(bf)),
        n16.deserialize(s16.dig(bf)),
        n17.deserialize(s17.dig(bf)),
        n18.deserialize(s18.dig(bf)),
        n19.deserialize(s19.dig(bf)),
        n20.deserialize(s20.dig(bf)),
        n21.deserialize(s21.dig(bf)),
        n22.deserialize(s22.dig(bf))
      )
    }
  }

}
