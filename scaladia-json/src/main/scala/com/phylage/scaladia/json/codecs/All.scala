package com.phylage.scaladia.json.codecs

import com.phylage.scaladia.json.codecs.definition.{AnyRefCodecsImpl, AnyValCodecs, TupleCodecsImpl}

private[json] trait All extends AnyValCodecs with AnyRefCodecsImpl with TupleCodecsImpl {
}
