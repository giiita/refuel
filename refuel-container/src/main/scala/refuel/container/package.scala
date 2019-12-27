package refuel

import refuel.injector.scope.IndexedSymbol
import refuel.runtime.InjectionReflector

import scala.collection.concurrent.TrieMap
import scala.reflect.internal.util.WeakHashSet

package object container {
  type ContainerPool = TrieMap[ContainerIndexedKey, WeakHashSet[IndexedSymbol[_]]]

  private[container] implicit val injectionReflector: InjectionReflector = RuntimeReflector
}
