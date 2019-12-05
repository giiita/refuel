package refuel.json.codecs.builder.context.translation
import refuel.json.Codec
import refuel.json.codecs.definition.TupleCodecsImpl

trait TupleCodecTranslator extends TupleCodecsImpl {
  def tuple[A: Codec, B: Codec]: Codec[(A, B)] = tuple2
  def tuple[A: Codec, B: Codec, C: Codec]: Codec[(A, B, C)] = tuple3
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec]: Codec[(A, B, C, D)] = tuple4
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec]: Codec[(A, B, C, D, E)] = tuple5
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec]: Codec[(A, B, C, D, E, F)] = tuple6
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec]: Codec[(A, B, C, D, E, F, G)] = tuple7
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec]: Codec[(A, B, C, D, E, F, G, H)] = tuple8
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec]: Codec[(A, B, C, D, E, F, G, H, I)] = tuple9
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J)] = tuple10
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K)] = tuple11
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L)] = tuple12
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = tuple13
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = tuple14
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = tuple15
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = tuple16
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = tuple17
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = tuple18
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = tuple19
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = tuple20
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = tuple21
  def tuple[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]: Codec[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = tuple22
}
