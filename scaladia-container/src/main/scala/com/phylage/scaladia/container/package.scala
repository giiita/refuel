package com.phylage.scaladia

import com.phylage.scaladia.injector.scope.IndexedSymbol
import com.phylage.scaladia.runtime.InjectionReflector

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

package object container {
  type ContainerPool = TrieMap[ContainerIndexedKey, mutable.LinkedHashSet[IndexedSymbol[_]]]

  private[container] implicit val injectionReflector: InjectionReflector = RuntimeReflector
}
