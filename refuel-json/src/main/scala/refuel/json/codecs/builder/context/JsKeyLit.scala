package refuel.json.codecs.builder.context

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.builder.CBuildComp
import refuel.json.codecs.builder.CBuildComp._
import refuel.json.{Codec, Json}

/**
 * Json key literal builder.
 * Used to synthesize Codec.
 *
 * Build practices.
 * {{{
 *   ("root" / "next" / "terminal")
 * }}}
 *
 * @param v json key literal set
 */
case class JsKeyLit(v: Seq[String]) extends JsKeyLitOps {
  self =>
  /**
   * Set the key literal to add and rebuild.
   *
   * @param add Next key literal to add
   * @return
   */
  def /(add: String): JsKeyLit = JsKeyLit(v :+ add)

  def ++(that: JsKeyLitOps): JsKeyLitOps = copy(v ++ that.v)

  /**
   * Generate a Codec to serialize / deserialize this constructed literal.
   *
   * {{{
   *   ("root" / "next" / "terminal").parsed(CaseClassCodec.from[XXX])
   * }}}
   *
   * @tparam A Internal codec type
   * @return
   */
  def parsed[A: Codec]: CBuildComp[A] = new CBuildComp[A] {
    override private[json] val k: JsKeyLitOps = self.++(implicitly[Codec[A]].keyLiteralRef)
  }

  def parsed[A: Codec, B: Codec]
  : CBuildComp2[A, B] = new CBuildComp2(
    parsed[A],
    parsed[B]
  )

  def parsed[A: Codec, B: Codec, C: Codec]
  : CBuildComp3[A, B, C] = new CBuildComp3(
    parsed[A],
    parsed[B],
    parsed[C]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec]
  : CBuildComp4[A, B, C, D] = new CBuildComp4(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]
  : CBuildComp5[A, B, C, D, E] = new CBuildComp5(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]
  : CBuildComp6[A, B, C, D, E, F] = new CBuildComp6(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]
  : CBuildComp7[A, B, C, D, E, F, G] = new CBuildComp7(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]
  : CBuildComp8[A, B, C, D, E, F, G, H] = new CBuildComp8(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]
  : CBuildComp9[A, B, C, D, E, F, G, H, I] = new CBuildComp9(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec]
  : CBuildComp10[A, B, C, D, E, F, G, H, I, J] = new CBuildComp10(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec]
  : CBuildComp11[A, B, C, D, E, F, G, H, I, J, K] = new CBuildComp11(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec]
  : CBuildComp12[A, B, C, D, E, F, G, H, I, J, K, L] = new CBuildComp12(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec]
  : CBuildComp13[A, B, C, D, E, F, G, H, I, J, K, L, M] = new CBuildComp13(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec]
  : CBuildComp14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = new CBuildComp14(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec]
  : CBuildComp15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = new CBuildComp15(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec]
  : CBuildComp16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = new CBuildComp16(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec]
  : CBuildComp17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = new CBuildComp17(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P],
    parsed[Q]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec]
  : CBuildComp18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = new CBuildComp18(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P],
    parsed[Q],
    parsed[R]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec]
  : CBuildComp19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = new CBuildComp19(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P],
    parsed[Q],
    parsed[R],
    parsed[S]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec]
  : CBuildComp20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = new CBuildComp20(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P],
    parsed[Q],
    parsed[R],
    parsed[S],
    parsed[T]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec]
  : CBuildComp21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = new CBuildComp21(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P],
    parsed[Q],
    parsed[R],
    parsed[S],
    parsed[T],
    parsed[U]
  )

  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]
  : CBuildComp22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = new CBuildComp22(
    parsed[A],
    parsed[B],
    parsed[C],
    parsed[D],
    parsed[E],
    parsed[F],
    parsed[G],
    parsed[H],
    parsed[I],
    parsed[J],
    parsed[K],
    parsed[L],
    parsed[M],
    parsed[N],
    parsed[O],
    parsed[P],
    parsed[Q],
    parsed[R],
    parsed[S],
    parsed[T],
    parsed[U],
    parsed[V]
  )

  /**
   * Follow the target JsonObject recursively from the constructed key literal.
   *
   * @param x Root json object
   * @return
   */
  override def rec(x: Json): Json = {
    v.foldLeft(x)(_ named _)
  }

  /**
   * TODO: Temporary implementation
   *
   * @return
   */
  override def toString: String = v.mkString
}
