package com.phylage.scaladia.container

import com.phylage.scaladia.Main.getClass
import com.phylage.scaladia.injector.InjectionPool
import com.phylage.scaladia.runtime.InjectionReflector

import scala.reflect.runtime.universe

class RuntimeReflector extends InjectionReflector {
  val mirror: universe.Mirror = universe.runtimeMirror(getClass.getClassLoader)

  override def reflect(pool: InjectionPool): Unit = ???
}
