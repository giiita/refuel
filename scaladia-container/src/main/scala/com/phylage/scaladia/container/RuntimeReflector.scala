package com.phylage.scaladia.container

import java.net.URLClassLoader

import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.scope.IndexedSymbol
import com.phylage.scaladia.runtime.InjectionReflector

import scala.annotation.tailrec
import scala.reflect.runtime.universe


object RuntimeReflector extends InjectionReflector {
  def classloader: URLClassLoader = getUrlClassloader(getClass.getClassLoader)

  def mirror: universe.Mirror = universe.runtimeMirror(classloader)

  /**
    * Create injection applyment.
    *
    * @param symbols module symbols
    * @tparam T injection type
    * @return
    */
  override def reflectClass[T: universe.WeakTypeTag](c: Container)(symbols: Set[universe.ClassSymbol]): Set[IndexedSymbol[T]] = {
    symbols.map { x =>
      mirror.reflectClass(x)
        .reflectConstructor(x.primaryConstructor.asMethod)
        .apply()
        .asInstanceOf[AutoInject[T]] match {
        case ai => ai.flush(c)
      }
    }
  }

  /**
    * Create injection applyment.
    *
    * @param symbols module symbols
    * @tparam T injection type
    * @return
    */
  override def reflectModule[T: universe.WeakTypeTag](c: Container)(symbols: Set[universe.ModuleSymbol]): Set[IndexedSymbol[T]] = {
    symbols.map { x =>
      mirror.reflectModule(x)
        .instance
        .asInstanceOf[AutoInject[T]] match {
        case ai => ai.flush(c)
      }
    }
  }

  /**
    * Reflect to a runtime class.
    *
    * @param t Type symbol.
    * @return
    */
  override def reflectClass(t: universe.Type): universe.RuntimeClass = {
    mirror.runtimeClass(t)
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
