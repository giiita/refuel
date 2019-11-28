package refuel.json.codecs

import refuel.json.codecs.definition.{AnyRefCodecsImpl, AnyValCodecs, TupleCodecsImpl}

private[json] trait All extends AnyValCodecs with AnyRefCodecsImpl with TupleCodecsImpl {
}
