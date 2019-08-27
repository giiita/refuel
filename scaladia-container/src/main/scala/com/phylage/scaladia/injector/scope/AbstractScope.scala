package com.phylage.scaladia.injector.scope

import scala.reflect.runtime.universe._

private[scaladia] abstract class AbstractScope[T](val tag: Type) extends IndexedSymbol[T]
