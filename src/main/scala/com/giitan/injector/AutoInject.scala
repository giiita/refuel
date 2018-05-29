package com.giitan.injector

import com.giitan.container.FunctIndexer
import com.giitan.scope.Scope
import scala.reflect.runtime.universe._

class AutoInject[X: TypeTag] extends Injector { me: X =>
  private[giitan] final def indexing(): Unit = {
    FunctIndexer.indexing(typeTag[X], me, me)
    Scope[X].acceptedGlobal()
  }
}