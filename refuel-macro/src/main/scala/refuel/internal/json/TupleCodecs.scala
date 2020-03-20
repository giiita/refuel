package refuel.internal.json

import refuel.json.Codec

trait TupleCodecs {
  implicit def __tuple2[A: Codec, B: Codec]: Codec[(A, B)]
  implicit def __tuple3[A: Codec, B: Codec, C: Codec]: Codec[(A, B, C)]
  implicit def __tuple4[A: Codec, B: Codec, C: Codec, D: Codec]: Codec[(A, B, C, D)]
  implicit def __tuple5[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]: Codec[(A, B, C, D, E)]
  implicit def __tuple6[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]: Codec[(A, B, C, D, E, F)]
  implicit def __tuple7[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]: Codec[(A, B, C, D, E, F, G)]
  implicit def __tuple8[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]: Codec[(A, B, C, D, E, F, G, H)]
  implicit def __tuple9[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]: Codec[(A, B, C, D, E, F, G, H, I)]
  implicit def __tuple10[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J)]
  implicit def __tuple11[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K)]
  implicit def __tuple12[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L)]
  implicit def __tuple13[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M)]
  implicit def __tuple14[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]
  implicit def __tuple15[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]
  implicit def __tuple16[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]
  implicit def __tuple17[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]
  implicit def __tuple18[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]
  implicit def __tuple19[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]
  implicit def __tuple20[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]
  implicit def __tuple21[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]
  implicit def __tuple22[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]
}
