package refuel

import refuel.injector.scope.IndexedSymbol
import refuel.runtime.InjectionReflector

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

package object container {
  type ContainerPool = TrieMap[Symbol, mutable.HashSet[IndexedSymbol[_]]]

  private[container] implicit val injectionReflector: InjectionReflector = RuntimeReflector
}
