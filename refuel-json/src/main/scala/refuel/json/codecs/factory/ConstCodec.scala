package refuel.json.codecs.factory

import refuel.internal.json.ConstructCodecFactory
import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.Codec

/**
  * Generate Codec with apply and unapply functions.
  * To generate codec, you need apply and unapply json key name,
  * so you need field name to adapt to the type type of apply function.
  *
  * {{{
  *   // strValue, intValue, datetime is json JsonKeyLiteralOps key name.
  *   ConstCodec.from("strValue", "intValue", "datetime")
  *                  ((s, i, d) => Obj(s, i, d))
  *                  (z => Some((z.s, z.i, z.d)))
  * }}}
  */
private[json] object ConstCodec {
  def pure[A, Z](apl: A => Z)(upl: Z => Option[A]): Codec[Z] = macro ConstructCodecFactory.fromConst0[A, Z]

  def from[A, Z](n1: JsonKeyRef)(apl: A => Z)(upl: Z => Option[A]): Codec[Z] =
    macro ConstructCodecFactory.fromConst1[A, Z]

  def from[A, B, Z](n1: JsonKeyRef, n2: JsonKeyRef)(apl: (A, B) => Z)(upl: Z => Option[(A, B)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst2[A, B, Z]

  def from[A, B, C, Z](n1: JsonKeyRef, n2: JsonKeyRef, n3: JsonKeyRef)(apl: (A, B, C) => Z)(
      upl: Z => Option[(A, B, C)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst3[A, B, C, Z]

  def from[A, B, C, D, Z](n1: JsonKeyRef, n2: JsonKeyRef, n3: JsonKeyRef, n4: JsonKeyRef)(apl: (A, B, C, D) => Z)(
      upl: Z => Option[(A, B, C, D)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst4[A, B, C, D, Z]

  def from[A, B, C, D, E, Z](n1: JsonKeyRef, n2: JsonKeyRef, n3: JsonKeyRef, n4: JsonKeyRef, n5: JsonKeyRef)(
      apl: (A, B, C, D, E) => Z
  )(upl: Z => Option[(A, B, C, D, E)]): Codec[Z] = macro ConstructCodecFactory.fromConst5[A, B, C, D, E, Z]

  def from[A, B, C, D, E, F, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef
  )(apl: (A, B, C, D, E, F) => Z)(upl: Z => Option[(A, B, C, D, E, F)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst6[A, B, C, D, E, F, Z]

  def from[A, B, C, D, E, F, G, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G) => Z)(upl: Z => Option[(A, B, C, D, E, F, G)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst7[A, B, C, D, E, F, G, Z]

  def from[A, B, C, D, E, F, G, H, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst8[A, B, C, D, E, F, G, H, Z]

  def from[A, B, C, D, E, F, G, H, I, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst9[A, B, C, D, E, F, G, H, I, Z]

  def from[A, B, C, D, E, F, G, H, I, J, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst10[A, B, C, D, E, F, G, H, I, J, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst11[A, B, C, D, E, F, G, H, I, J, K, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst12[A, B, C, D, E, F, G, H, I, J, K, L, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst13[A, B, C, D, E, F, G, H, I, J, K, L, M, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef,
      n17: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef,
      n17: JsonKeyRef,
      n18: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef,
      n17: JsonKeyRef,
      n18: JsonKeyRef,
      n19: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef,
      n17: JsonKeyRef,
      n18: JsonKeyRef,
      n19: JsonKeyRef,
      n20: JsonKeyRef
  )(apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)(
      upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]
  ): Codec[Z] = macro ConstructCodecFactory.fromConst20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef,
      n17: JsonKeyRef,
      n18: JsonKeyRef,
      n19: JsonKeyRef,
      n20: JsonKeyRef,
      n21: JsonKeyRef
  )(
      apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z
  )(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z](
      n1: JsonKeyRef,
      n2: JsonKeyRef,
      n3: JsonKeyRef,
      n4: JsonKeyRef,
      n5: JsonKeyRef,
      n6: JsonKeyRef,
      n7: JsonKeyRef,
      n8: JsonKeyRef,
      n9: JsonKeyRef,
      n10: JsonKeyRef,
      n11: JsonKeyRef,
      n12: JsonKeyRef,
      n13: JsonKeyRef,
      n14: JsonKeyRef,
      n15: JsonKeyRef,
      n16: JsonKeyRef,
      n17: JsonKeyRef,
      n18: JsonKeyRef,
      n19: JsonKeyRef,
      n20: JsonKeyRef,
      n21: JsonKeyRef,
      n22: JsonKeyRef
  )(
      apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z
  )(upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]): Codec[Z] =
    macro ConstructCodecFactory.fromConst22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z]
}
