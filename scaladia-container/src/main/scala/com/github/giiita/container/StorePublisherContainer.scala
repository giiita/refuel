package com.github.giiita.container

import com.github.giiita.injector.AutoInject
import com.github.giiita.injector.scope.{InjectableScope, OpenScope}

import scala.reflect.runtime.universe._

abstract class StorePublisherContainer extends Container with AutoInject[Container] { me =>

  override def flush[N <: Container](implicit x: WeakTypeTag[N]): InjectableScope[Container] = {
    OpenScope[Container](me, injectionPriority, x.asInstanceOf[WeakTypeTag[Container]])
  }
}
