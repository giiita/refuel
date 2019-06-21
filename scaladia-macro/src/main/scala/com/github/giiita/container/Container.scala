package com.github.giiita.container

import com.github.giiita.container.indexer.Indexer
import com.github.giiita.injector.scope.InjectableScope
import com.github.giiita.provider.Accessor

import scala.reflect.runtime.universe._

private[giiita] trait Container {
  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  private[giiita] def cache[T](value: InjectableScope[T]): InjectableScope[T]

  /**
    * May return an injectable object.
    *
    * @return
    */
  private[giiita] def find[T: WeakTypeTag](requestFrom: Accessor[_]): Option[T]

  /**
    * Generate an indexer.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  private[giiita] def createIndexer[T: WeakTypeTag](x: T, priority: Int): Indexer[T]
}
