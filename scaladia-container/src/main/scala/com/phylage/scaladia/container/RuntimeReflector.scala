package com.phylage.scaladia.container

import java.net.URLClassLoader

import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.InjectionPool.InjectionApplyment
import com.phylage.scaladia.runtime.InjectionReflector

import scala.annotation.tailrec
import scala.reflect.runtime.universe


object RuntimeReflector extends InjectionReflector {
  def classloader: URLClassLoader = getUrlClassloader(getClass.getClassLoader)
  def mirror: universe.Mirror = universe.runtimeMirror(classloader)

  /**
    * Create injection applyment.
    *
    * @param x module symbol
    * @tparam T injection type
    * @return
    */
  override def reflect[T: universe.WeakTypeTag](x: universe.ModuleSymbol): InjectionApplyment[T] = {
    mirror.reflectModule(x).instance.asInstanceOf[AutoInject[T]].flush
  }

  @tailrec
  private final def getUrlClassloader(currentClassLoader: ClassLoader, depth: Int = 0): URLClassLoader = {
    currentClassLoader match {
      case x: URLClassLoader => x
      case x if depth < 5 =>
        getUrlClassloader(x.getParent, depth + 1)
      case _ => throw new RuntimeException("URLClassloader can not be obtained.")
    }
  }
}
