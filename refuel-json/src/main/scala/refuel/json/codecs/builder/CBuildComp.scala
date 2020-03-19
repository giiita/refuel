package refuel.json.codecs.builder

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.Codec
import refuel.json.codecs.builder.CBuildComp.CBuildComp2
import refuel.json.codecs.factory.ConstCodec

/**
  * Internal Codec builder completion class.
  *
  * @tparam A Codec type for temporarily stacked children
  */
private[json] abstract class CBuildComp[A: Codec] {
  private[json] def k: JsonKeyRef

  private[json] def _c: Codec[A] = implicitly[Codec[A]]

  def apply[Z](apl: A => Z)(upl: Z => Option[A]): Codec[Z] = {
     ConstCodec.from(k)(apl)(upl)
  }

  def ++[Z](that: CBuildComp[Z]): CBuildComp2[A, Z] = {
    implicit val newc: Codec[Z] = that._c
    new CBuildComp2(this, that)
  }
}

object CBuildComp {

  class CBuildComp2[A: Codec, B: Codec]
  (a: CBuildComp[A], b: CBuildComp[B]) {
    def apply[Z](apl: (A, B) => Z)(upl: Z => Option[(A, B)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp3[A, B, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp3(a, b, that)
    }
  }

  class CBuildComp3[A: Codec, B: Codec, C: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C]) {
    def apply[Z](apl: (A, B, C) => Z)(upl: Z => Option[(A, B, C)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp4[A, B, C, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp4(a, b, c, that)
    }
  }

  class CBuildComp4[A: Codec, B: Codec, C: Codec, D: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D]) {
    def apply[Z](apl: (A, B, C, D) => Z)(upl: Z => Option[(A, B, C, D)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp5[A, B, C, D, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp5(a, b, c, d, that)
    }
  }

  class CBuildComp5[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E]) {
    def apply[Z](apl: (A, B, C, D, E) => Z)(upl: Z => Option[(A, B, C, D, E)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp6[A, B, C, D, E, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp6(a, b, c, d, e, that)
    }
  }

  class CBuildComp6[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F]) {
    def apply[Z](apl: (A, B, C, D, E, F) => Z)(upl: Z => Option[(A, B, C, D, E, F)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp7[A, B, C, D, E, F, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp7(a, b, c, d, e, f, that)
    }
  }

  class CBuildComp7[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G]) {
    def apply[Z](apl: (A, B, C, D, E, F, G) => Z)(upl: Z => Option[(A, B, C, D, E, F, G)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp8[A, B, C, D, E, F, G, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp8(a, b, c, d, e, f, g, that)
    }
  }

  class CBuildComp8[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp9[A, B, C, D, E, F, G, H, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp9(a, b, c, d, e, f, g, h, that)
    }
  }

  class CBuildComp9[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp10[A, B, C, D, E, F, G, H, I, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp10(a, b, c, d, e, f, g, h, i, that)
    }
  }

  class CBuildComp10[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp11[A, B, C, D, E, F, G, H, I, J, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp11(a, b, c, d, e, f, g, h, i, j, that)
    }
  }

  class CBuildComp11[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp12[A, B, C, D, E, F, G, H, I, J, K, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp12(a, b, c, d, e, f, g, h, i, j, k, that)
    }
  }

  class CBuildComp12[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp13[A, B, C, D, E, F, G, H, I, J, K, L, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp13(a, b, c, d, e, f, g, h, i, j, k, l, that)
    }
  }

  class CBuildComp13[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp14[A, B, C, D, E, F, G, H, I, J, K, L, M, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp14(a, b, c, d, e, f, g, h, i, j, k, l, m, that)
    }
  }

  class CBuildComp14[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp15(a, b, c, d, e, f, g, h, i, j, k, l, m, n, that)
    }
  }

  class CBuildComp15[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp16(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, that)
    }
  }

  class CBuildComp16[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp17(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, that)
    }
  }

  class CBuildComp17[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P], q: CBuildComp[Q]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp18(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, that)
    }
  }

  class CBuildComp18[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P], q: CBuildComp[Q], r: CBuildComp[R]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp19(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, that)
    }
  }

  class CBuildComp19[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P], q: CBuildComp[Q], r: CBuildComp[R], s: CBuildComp[S]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp20(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, that)
    }
  }

  class CBuildComp20[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P], q: CBuildComp[Q], r: CBuildComp[R], s: CBuildComp[S], t: CBuildComp[T]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k, t.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp21(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, that)
    }
  }

  class CBuildComp21[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P], q: CBuildComp[Q], r: CBuildComp[R], s: CBuildComp[S], t: CBuildComp[T], u: CBuildComp[U]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k, t.k, u.k)(apl)(upl)
    }

    def ++[Z]
    (that: CBuildComp[Z])
    : CBuildComp22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z] = {
      implicit val newc: Codec[Z] = that._c
      new CBuildComp22(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, that)
    }
  }

  class CBuildComp22[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]
  (a: CBuildComp[A], b: CBuildComp[B], c: CBuildComp[C], d: CBuildComp[D], e: CBuildComp[E], f: CBuildComp[F], g: CBuildComp[G], h: CBuildComp[H], i: CBuildComp[I], j: CBuildComp[J], k: CBuildComp[K], l: CBuildComp[L], m: CBuildComp[M], n: CBuildComp[N], o: CBuildComp[O], p: CBuildComp[P], q: CBuildComp[Q], r: CBuildComp[R], s: CBuildComp[S], t: CBuildComp[T], u: CBuildComp[U], v: CBuildComp[V]) {
    def apply[Z](apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z)
                (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]): Codec[Z] = {
      ConstCodec.from(a.k, b.k, c.k, d.k, e.k, f.k, g.k, h.k, i.k, j.k, k.k, l.k, m.k, n.k, o.k, p.k, q.k, r.k, s.k, t.k, u.k, v.k)(apl)(upl)
    }
  }

}