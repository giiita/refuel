package refuel.json.codecs.builder.context.translation
import refuel.json.Codec
import refuel.json.codecs.definition.TupleCodecsImpl

trait TupleCodecTranslator extends TupleCodecsImpl {
  protected def tuple[A: Codec, B: Codec]: Codec[(A, B)]                                                     = __tuple2
  protected def tuple[A: Codec, B: Codec, C: Codec]: Codec[(A, B, C)]                                        = __tuple3
  protected def tuple[A: Codec, B: Codec, C: Codec, D: Codec]: Codec[(A, B, C, D)]                           = __tuple4
  protected def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]: Codec[(A, B, C, D, E)]              = __tuple5
  protected def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]: Codec[(A, B, C, D, E, F)] = __tuple6
  protected def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]
      : Codec[(A, B, C, D, E, F, G)] = __tuple7
  protected def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]
      : Codec[(A, B, C, D, E, F, G, H)] = __tuple8
  protected def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]
      : Codec[(A, B, C, D, E, F, G, H, I)] = __tuple9
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J)] = __tuple10
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K)] = __tuple11
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L)] = __tuple12
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = __tuple13
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = __tuple14
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = __tuple15
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = __tuple16
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec,
      Q: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = __tuple17
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec,
      Q: Codec,
      R: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = __tuple18
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec,
      Q: Codec,
      R: Codec,
      S: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = __tuple19
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec,
      Q: Codec,
      R: Codec,
      S: Codec,
      T: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = __tuple20
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec,
      Q: Codec,
      R: Codec,
      S: Codec,
      T: Codec,
      U: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = __tuple21
  protected def tuple[
      A: Codec,
      B: Codec,
      C: Codec,
      D: Codec,
      E: Codec,
      F: Codec,
      G: Codec,
      H: Codec,
      I: Codec,
      J: Codec,
      K: Codec,
      L: Codec,
      M: Codec,
      N: Codec,
      O: Codec,
      P: Codec,
      Q: Codec,
      R: Codec,
      S: Codec,
      T: Codec,
      U: Codec,
      V: Codec
  ]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = __tuple22
}
