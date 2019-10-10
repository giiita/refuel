package com.phylage.scaladia.internal.json

import com.phylage.scaladia.json.Codec

trait TupleCodecs {
  implicit def tuple2[A: Codec, B: Codec]: Codec[(A, B)]
  implicit def tuple3[A: Codec, B: Codec, C: Codec]: Codec[(A, B, C)]
  implicit def tuple4[A: Codec, B: Codec, C: Codec, D: Codec]: Codec[(A, B, C, D)]
  implicit def tuple5[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]: Codec[(A, B, C, D, E)]
  implicit def tuple6[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]: Codec[(A, B, C, D, E, F)]
  implicit def tuple7[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]: Codec[(A, B, C, D, E, F, G)]
  implicit def tuple8[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]: Codec[(A, B, C, D, E, F, G, H)]
  implicit def tuple9[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]: Codec[(A, B, C, D, E, F, G, H, I)]
  implicit def tuple10[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J)]
  implicit def tuple11[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K)]
  implicit def tuple12[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L)]
  implicit def tuple13[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M)]
  implicit def tuple14[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]
  implicit def tuple15[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]
  implicit def tuple16[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]
  implicit def tuple17[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]
  implicit def tuple18[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]
  implicit def tuple19[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]
  implicit def tuple20[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]
  implicit def tuple21[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]
  implicit def tuple22[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]
}
