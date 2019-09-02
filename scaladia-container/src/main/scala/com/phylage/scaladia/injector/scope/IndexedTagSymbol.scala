package com.phylage.scaladia.injector.scope

import scala.reflect.runtime.universe._

private[scaladia] abstract class IndexedTagSymbol[T](val tag: Type) extends IndexedSymbol[T]
