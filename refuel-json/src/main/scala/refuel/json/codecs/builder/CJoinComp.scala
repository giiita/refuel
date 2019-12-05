package refuel.json.codecs.builder

import refuel.json.error.DeserializeFailed
import refuel.json.{Codec, Json}

/**
  * Internal Codec builder completion class.
  *
  * @tparam A Codec type for temporarily stacked children
  */
private[json] abstract class CJoinComp[A: Codec] {
  def apply[Z](apl: A => Z)(upl: Z => Option[A]): Codec[Z] = new Codec[Z] {

    /**
      * Serialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param t Serializable object.
      * @return
      */
    override def serialize(t: Z): Json = ???

    /**
      * Deserialize Json to T format.
      * Failure should continue and propagate up.
      *
      * @param bf Json syntax tree.
      * @return
      */
    override def deserialize(
                              bf: Json
                            ): Either[DeserializeFailed, Z] = ???
  }

  //  def ++[Z](that: Codec[Z]): CJoinComp2[A, Z] = {
  //    implicit val newc: Codec[Z] = that._c
  //    new CJoinComp2(this, that)
  //  }
}

object CJoinComp {

  //  class CJoinComp2[A: Codec, B: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B]) {
  //    def apply[Z](apl: (A, B) => Z)(upl: Z => Option[(A, B)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp3[A, B, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp3(a, b, that)
  //    }
  //  }
  //
  //  class CJoinComp3[A: Codec, B: Codec, C: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C]) {
  //    def apply[Z](apl: (A, B, C) => Z)(upl: Z => Option[(A, B, C)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp4[A, B, C, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp4(a, b, c, that)
  //    }
  //  }
  //
  //  class CJoinComp4[A: Codec, B: Codec, C: Codec, D: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D]) {
  //    def apply[Z](apl: (A, B, C, D) => Z)(upl: Z => Option[(A, B, C, D)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp5[A, B, C, D, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp5(a, b, c, d, that)
  //    }
  //  }
  //
  //  class CJoinComp5[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E]) {
  //    def apply[Z](apl: (A, B, C, D, E) => Z)(upl: Z => Option[(A, B, C, D, E)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp6[A, B, C, D, E, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp6(a, b, c, d, e, that)
  //    }
  //  }
  //
  //  class CJoinComp6[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F]) {
  //    def apply[Z](apl: (A, B, C, D, E, F) => Z)(upl: Z => Option[(A, B, C, D, E, F)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp7[A, B, C, D, E, F, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp7(a, b, c, d, e, f, that)
  //    }
  //  }
  //
  //  class CJoinComp7[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G) => Z)(upl: Z => Option[(A, B, C, D, E, F, G)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp8[A, B, C, D, E, F, G, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp8(a, b, c, d, e, f, g, that)
  //    }
  //  }
  //
  //  class CJoinComp8[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp9[A, B, C, D, E, F, G, H, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp9(a, b, c, d, e, f, g, h, that)
  //    }
  //  }
  //
  //  class CJoinComp9[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp10[A, B, C, D, E, F, G, H, I, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp10(a, b, c, d, e, f, g, h, i, that)
  //    }
  //  }
  //
  //  class CJoinComp10[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp11[A, B, C, D, E, F, G, H, I, J, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp11(a, b, c, d, e, f, g, h, i, j, that)
  //    }
  //  }
  //
  //  class CJoinComp11[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp12[A, B, C, D, E, F, G, H, I, J, K, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp12(a, b, c, d, e, f, g, h, i, j, k, that)
  //    }
  //  }
  //
  //  class CJoinComp12[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp13[A, B, C, D, E, F, G, H, I, J, K, L, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp13(a, b, c, d, e, f, g, h, i, j, k, l, that)
  //    }
  //  }
  //
  //  class CJoinComp13[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp14[A, B, C, D, E, F, G, H, I, J, K, L, M, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp14(a, b, c, d, e, f, g, h, i, j, k, l, m, that)
  //    }
  //  }
  //
  //  class CJoinComp14[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp15(a, b, c, d, e, f, g, h, i, j, k, l, m, n, that)
  //    }
  //  }
  //
  //  class CJoinComp15[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp16(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, that)
  //    }
  //  }
  //
  //  class CJoinComp16[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp17(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, that)
  //    }
  //  }
  //
  //  class CJoinComp17[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P], q: CJoinComp[Q]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp18(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, that)
  //    }
  //  }
  //
  //  class CJoinComp18[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P], q: CJoinComp[Q], r: CJoinComp[R]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp19(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, that)
  //    }
  //  }
  //
  //  class CJoinComp19[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P], q: CJoinComp[Q], r: CJoinComp[R], s: CJoinComp[S]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp20(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, that)
  //    }
  //  }
  //
  //  class CJoinComp20[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P], q: CJoinComp[Q], r: CJoinComp[R], s: CJoinComp[S], t: CJoinComp[T]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k, t.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp21(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, that)
  //    }
  //  }
  //
  //  class CJoinComp21[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P], q: CJoinComp[Q], r: CJoinComp[R], s: CJoinComp[S], t: CJoinComp[T], u: CJoinComp[U]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k, t.k, u.k)(apl)(upl)
  //    }
  //
  //    def ++[Z]
  //    (that: CJoinComp[Z])
  //    : CJoinComp22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z] = {
  //      implicit val newc: Codec[Z] = that._c
  //      new CJoinComp22(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, that)
  //    }
  //  }
  //
  //  class CJoinComp22[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]
  //  (a: CJoinComp[A], b: CJoinComp[B], c: CJoinComp[C], d: CJoinComp[D], e: CJoinComp[E], f: CJoinComp[F], g: CJoinComp[G], h: CJoinComp[H], i: CJoinComp[I], j: CJoinComp[J], k: CJoinComp[K], l: CJoinComp[L], m: CJoinComp[M], n: CJoinComp[N], o: CJoinComp[O], p: CJoinComp[P], q: CJoinComp[Q], r: CJoinComp[R], s: CJoinComp[S], t: CJoinComp[T], u: CJoinComp[U], v: CJoinComp[V]) {
  //    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z)
  //                (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]): Codec[Z] = {
  //      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k, t.k, u.k, v.k)(apl)(upl)
  //    }
  //  }

}
