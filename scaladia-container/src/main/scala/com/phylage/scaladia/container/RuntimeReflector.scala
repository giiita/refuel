package com.phylage.scaladia.container

import com.phylage.scaladia.container.RuntimeInjectionPool.InjectionApplyment
import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.runtime.InjectionReflector

import scala.reflect.runtime.universe


class RuntimeReflector extends InjectionReflector {
  val mirror: universe.Mirror = universe.runtimeMirror(getClass.getClassLoader)

  override def reflect[T: universe.WeakTypeTag](x: universe.ModuleSymbol): InjectionApplyment[T] = {
    mirror.reflectModule(x).instance.asInstanceOf[AutoInject[T]].flush
  }
}
