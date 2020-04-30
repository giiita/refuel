package refuel.json

import refuel.json.codecs.All
import refuel.json.codecs.builder.context.CodecBuildFeature

/**
 * Every interface for defining codecs is defined.
 * The interface used for the definition is this.
 *
 * - Serialize(constructor)
 * - Deserialize(constructor)
 * - Format(constructor)(constructor)
 *
 * - CaseClassCodec.from[T]
 * - ConstCodec.from(jsonkeyName...)(apply)(unapply)
 *
 * - tuple(codec)
 * - set(codec)
 * - seq(codec)
 * - vector(codec)
 * - array(codec)
 * - option(codec)
 */
trait CodecDef extends All with CodecBuildFeature {
  protected final val CaseClassCodec = refuel.json.codecs.factory.CaseClassCodec
  protected final val ConstCodec = refuel.json.codecs.factory.ConstCodec
}
