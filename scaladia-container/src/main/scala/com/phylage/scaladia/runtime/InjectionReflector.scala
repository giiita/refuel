package com.phylage.scaladia.runtime

import com.phylage.scaladia.injector.InjectionPool

trait InjectionReflector {
  def reflect(pool: InjectionPool): Unit
}
