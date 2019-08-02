package com.phylage.scaladia.injector.scope

import scala.reflect.runtime.universe._

abstract class AbstractScope[T](val tag: WeakTypeTag[T]) extends InjectableScope[T]
