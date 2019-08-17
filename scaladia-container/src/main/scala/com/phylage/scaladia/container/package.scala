package com.phylage.scaladia

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.injector.scope.InjectableScope
import com.phylage.scaladia.runtime.InjectionReflector

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

package object container extends Injector {
  type ContainerPool = TrieMap[ContainerIndexedKey, mutable.LinkedHashSet[InjectableScope[_]]]

  private[container] implicit val injectionReflector: InjectionReflector = RuntimeReflector
}
