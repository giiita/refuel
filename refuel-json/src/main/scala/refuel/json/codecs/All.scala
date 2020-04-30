package refuel.json.codecs

import refuel.json.codecs.definition.{AnyRefCodecsExport, AnyValCodecs, TupleCodecsImpl}

private[json] trait All extends AnyValCodecs with AnyRefCodecsExport with TupleCodecsImpl {}
