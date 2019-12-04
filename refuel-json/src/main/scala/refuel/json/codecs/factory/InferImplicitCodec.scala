package refuel.json.codecs.factory
import refuel.internal.json.CaseCodecFactory
import refuel.json.Codec

object InferImplicitCodec {
  def from[T]: Codec[T] = macro CaseCodecFactory.fromInferOrCase[T]
}
