package com.github.giiita.injector

import com.github.giiita.`macro`.Macro
import com.github.giiita.container.indexer.Indexer
import com.github.giiita.container.{Container, ContainerStore}
import com.github.giiita.provider.{Accessor, Lazy, Tag}

import scala.reflect.runtime.universe._

trait Injector {
  me =>

  protected def flush[T: WeakTypeTag](x: T, priority: Int = 1100): Unit = implicitly[Container].createIndexer(x, priority).indexing()

  protected def narrow[T: WeakTypeTag](x: T, priority: Int = Int.MaxValue): Indexer[T] = implicitly[Container].createIndexer(x, priority)

  protected def inject[T](implicit ctn: Container, access: Accessor[_]): Lazy[T] = macro Macro.inject[T]

  import scala.language.implicitConversions

  /**
    * Provide dependency.
    *
    * @param variable Stored dependency object.
    * @tparam X
    * @return
    */
  implicit def provide[X](variable: Lazy[X]): X = variable.provide

  implicit def from: Accessor[_] = Accessor(me)

  implicit def getContainer: Container = implicitly[ContainerStore].ctn

  type @@[+T, +U] = T with Tag[U]
}
