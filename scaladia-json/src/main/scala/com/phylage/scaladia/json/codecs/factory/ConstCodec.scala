package com.phylage.scaladia.json.codecs.factory

import com.phylage.scaladia.internal.json.ConstructCodecFactory
import com.phylage.scaladia.json.Codec

/**
  * Generate Codec with apply and unapply functions.
  * To generate codec, you need apply and unapply json key name,
  * so you need field name to adapt to the type type of apply function.
  *
  * {{{
  *   ConstCodec.from("strValue", "intValue", "datetime")
  *                  ((s, i, d) => Obj(s, i, d))
  *                  (z => Some((z.s, z.i, z.d)))
  * }}}
  */
object ConstCodec {
  def from[A, Z](n1: String)
                (apl: A => Z)
                (upl: Z => Option[A]): Codec[Z] = macro ConstructCodecFactory.fromConst1[A, Z]

  def from[A, B, Z](n1: String, n2: String)
                    (apl: (A, B) => Z)
                    (upl: Z => Option[(A, B)]): Codec[Z] = macro ConstructCodecFactory.fromConst2[A, B, Z]

  def from[A, B, C, Z](n1: String, n2: String, n3: String)
                      (apl: (A, B, C) => Z)
                      (upl: Z => Option[(A, B, C)]): Codec[Z] = macro ConstructCodecFactory.fromConst3[A, B, C, Z]

  def from[A, B, C, D, Z](n1: String, n2: String, n3: String, n4: String)
                         (apl: (A, B, C, D) => Z)
                         (upl: Z => Option[(A, B, C, D)]): Codec[Z] = macro ConstructCodecFactory.fromConst4[A, B, C, D, Z]

  def from[A, B, C, D, E, Z](n1: String, n2: String, n3: String, n4: String, n5: String)
                            (apl: (A, B, C, D, E) => Z)
                            (upl: Z => Option[(A, B, C, D, E)]): Codec[Z] = macro ConstructCodecFactory.fromConst5[A, B, C, D, E, Z]

  def from[A, B, C, D, E, F, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String)
                               (apl: (A, B, C, D, E, F) => Z)
                               (upl: Z => Option[(A, B, C, D, E, F)]): Codec[Z] = macro ConstructCodecFactory.fromConst6[A, B, C, D, E, F, Z]

  def from[A, B, C, D, E, F, G, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String)
                                  (apl: (A, B, C, D, E, F, G) => Z)
                                  (upl: Z => Option[(A, B, C, D, E, F, G)]): Codec[Z] = macro ConstructCodecFactory.fromConst7[A, B, C, D, E, F, G, Z]

  def from[A, B, C, D, E, F, G, H, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String)
                                     (apl: (A, B, C, D, E, F, G, H) => Z)
                                     (upl: Z => Option[(A, B, C, D, E, F, G, H)]): Codec[Z] = macro ConstructCodecFactory.fromConst8[A, B, C, D, E, F, G, H, Z]

  def from[A, B, C, D, E, F, G, H, I, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String)
                                        (apl: (A, B, C, D, E, F, G, H, I) => Z)
                                        (upl: Z => Option[(A, B, C, D, E, F, G, H, I)]): Codec[Z] = macro ConstructCodecFactory.fromConst9[A, B, C, D, E, F, G, H, I, Z]

  def from[A, B, C, D, E, F, G, H, I, J, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String)
                                           (apl: (A, B, C, D, E, F, G, H, I, J) => Z)
                                           (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J)]): Codec[Z] = macro ConstructCodecFactory.fromConst10[A, B, C, D, E, F, G, H, I, J, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String)
                                              (apl: (A, B, C, D, E, F, G, H, I, J, K) => Z)
                                              (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K)]): Codec[Z] = macro ConstructCodecFactory.fromConst11[A, B, C, D, E, F, G, H, I, J, K, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String)
                                                 (apl: (A, B, C, D, E, F, G, H, I, J, K, L) => Z)
                                                 (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L)]): Codec[Z] = macro ConstructCodecFactory.fromConst12[A, B, C, D, E, F, G, H, I, J, K, L, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String)
                                                    (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M) => Z)
                                                    (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]): Codec[Z] = macro ConstructCodecFactory.fromConst13[A, B, C, D, E, F, G, H, I, J, K, L, M, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String)
                                                       (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) => Z)
                                                       (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]): Codec[Z] = macro ConstructCodecFactory.fromConst14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String)
                                                          (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) => Z)
                                                          (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]): Codec[Z] = macro ConstructCodecFactory.fromConst15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String)
                                                             (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) => Z)
                                                             (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]): Codec[Z] = macro ConstructCodecFactory.fromConst16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String, n17: String)
                                                                (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) => Z)
                                                                (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]): Codec[Z] = macro ConstructCodecFactory.fromConst17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String, n17: String, n18: String)
                                                                   (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) => Z)
                                                                   (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]): Codec[Z] = macro ConstructCodecFactory.fromConst18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String, n17: String, n18: String, n19: String)
                                                                      (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) => Z)
                                                                      (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]): Codec[Z] = macro ConstructCodecFactory.fromConst19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String, n17: String, n18: String, n19: String, n20: String)
                                                                         (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) => Z)
                                                                         (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]): Codec[Z] = macro ConstructCodecFactory.fromConst20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String, n17: String, n18: String, n19: String, n20: String, n21: String)
                                                                            (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) => Z)
                                                                            (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]): Codec[Z] = macro ConstructCodecFactory.fromConst21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Z]

  def from[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z](n1: String, n2: String, n3: String, n4: String, n5: String, n6: String, n7: String, n8: String, n9: String, n10: String, n11: String, n12: String, n13: String, n14: String, n15: String, n16: String, n17: String, n18: String, n19: String, n20: String, n21: String, n22: String)
                                                                               (apl: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) => Z)
                                                                               (upl: Z => Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]): Codec[Z] = macro ConstructCodecFactory.fromConst22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Z]
}
