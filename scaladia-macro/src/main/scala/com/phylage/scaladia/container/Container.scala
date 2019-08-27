package com.phylage.scaladia.container

import com.phylage.scaladia.Types.{@@, Localized}
import com.phylage.scaladia.container.indexer.Indexer
import com.phylage.scaladia.effect.EffectLike
import com.phylage.scaladia.injector.scope.{IndexedSymbol, TypedAcceptContext}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

private[scaladia] trait Container {

  private[scaladia] val lights: Vector[Container]

  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  private[scaladia] def cache[T](value: IndexedSymbol[T]): IndexedSymbol[T]

  /**
    * May return an injectable object.
    *
    * @param requestFrom Inject caller.
    * @return
    */
  private[scaladia] def find[T, A: TypedAcceptContext](tpe: universe.Type, requestFrom: A): Option[T]

  /**
    * May return an injectable object.
    *
    * @param requestFrom Inject caller.
    * @return
    */
  private[scaladia] def find[T: WeakTypeTag, A: TypedAcceptContext](requestFrom: A): Option[T]

  /**
    * May return an injectable object.
    *
    * @return
    */
  private[scaladia] def findEffect: Set[EffectLike]

  /**
    * Generate an indexer.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  private[scaladia] def createIndexer[T: WeakTypeTag](x: T, priority: Int, lights: Vector[Container] = this.lights): Indexer[T]

  /**
    * Generate open scope.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  private[scaladia] def createScope[T: WeakTypeTag](x: T, priority: Int): IndexedSymbol[T]

  private[scaladia] def shading: Container @@ Localized
}
