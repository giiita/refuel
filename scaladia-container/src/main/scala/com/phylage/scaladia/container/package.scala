package com.phylage.scaladia

import com.phylage.scaladia.injector.scope.{IndexedSymbol, TypedAcceptContext}
import com.phylage.scaladia.provider.Accessor
import com.phylage.scaladia.runtime.InjectionReflector

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

package object container {
  type ContainerPool = TrieMap[ContainerIndexedKey, mutable.LinkedHashSet[IndexedSymbol[_]]]

  private[container] implicit val injectionReflector: InjectionReflector = RuntimeReflector

  private[container] implicit case object ClassTypeAcceptContext extends TypedAcceptContext[Class[_]] {
    override def accepted: IndexedSymbol[_] => Class[_] => Boolean = { x => y =>
      x.isOpen || x.acceptedClass(y.getClass)
    }
  }
}
