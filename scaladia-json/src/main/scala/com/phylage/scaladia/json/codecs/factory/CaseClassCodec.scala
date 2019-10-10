package com.phylage.scaladia.json.codecs.factory

import com.phylage.scaladia.internal.json.CaseCodecFactory
import com.phylage.scaladia.json.Codec

/**
  * Case class codec generator.
  *
  * Because it is generated by Macro,
  * Codec cannot be generated via a function that receives a type type.
  *
  * {{{
  *   implicit val codec: Codec[Z] = CaseClassCodec.from[Z]
  * }}}
  *
  * If a child codec can be obtained from a normal implicit scope,
  * it is preferentially incorporated into the Codec.
  *
  * In other words, because the codec of Tuple, AnyRef and AnyVal is used implicitly,
  * the scope of [[com.phylage.scaladia.json.JTransform]] is required.
  *
  * Currently, any AnyVal, AnyRef, Tuples codec override is not supported.
  * In the future, the codec will be indexed into the DI container and will be handled by the injection priority as before.
  */
object CaseClassCodec {
  def from[T]: Codec[T] = macro CaseCodecFactory.fromCaseClass[T]
}
