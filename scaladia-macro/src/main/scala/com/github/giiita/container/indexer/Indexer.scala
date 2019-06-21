package com.github.giiita.container.indexer

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.InjectableScope

import scala.reflect.ClassTag

/**
  * This is an indexer to register new objects in the injection container.
  *
  * @tparam T
  */
trait Indexer[T] { me =>
  /**
    * Create a new object in the injection container.
    *
    * @param ctn Container
    * @return
    */
  def indexing()(implicit ctn: Container): InjectableScope[T]

  /**
    * Create a new authorization class for this indexer.
    * If you do this, you will not be able to register the authorization instance.
    *
    * @tparam X Accept class
    * @return
    */
  def accept[X: ClassTag]: Indexer[T]

  /**
    * Register a new authorization instance with this indexer.
    * If you do this, you will not be able to register the authorization class.
    *
    * @param x Accept instance
    * @tparam X Accept instance type
    * @return
    */
  def accept[X](x: X): Indexer[T]

  implicit def self: Indexer[T] = me
}
