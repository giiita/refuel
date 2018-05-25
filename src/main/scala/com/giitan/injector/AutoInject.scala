package com.giitan.injector

import com.giitan.container.FunctIndexer
import com.giitan.scope.Scope
import scala.reflect.runtime.universe._

class AutoInjectThroughTag[X: TypeTag] extends AutoInjector { me: X =>
  FunctIndexer.indexing(typeTag[X], me, me)
  Scope[X].acceptedGlobal()
}

trait AutoInject[X] extends AutoInjectThroughTag[X] {
  me: X =>
}