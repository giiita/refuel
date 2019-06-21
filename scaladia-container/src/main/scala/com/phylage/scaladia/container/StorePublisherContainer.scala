package com.phylage.scaladia.container

import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.scope.{InjectableScope, OpenScope}

import scala.reflect.runtime.universe._

/**
  * It is an abstract container
  */
abstract class StorePublisherContainer extends Container with AutoInject[Container] {
  me =>

  /**
    * The process that is originally registered to "Injection Container" is prevented to prevent circular reference.
    *
    * @param x runtime weak type tag
    * @tparam N extends container type
    * @return
    */
  override final def flush[N <: Container](implicit x: WeakTypeTag[N]): InjectableScope[Container] = {
    OpenScope[Container](me, injectionPriority, x.asInstanceOf[WeakTypeTag[Container]])
  }
}
