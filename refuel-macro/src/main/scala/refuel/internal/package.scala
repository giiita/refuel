package refuel

import refuel.container.Container
import refuel.injector.scope.{IndexedSymbol, TypedAcceptContext}
import refuel.provider.Accessor

import scala.collection.concurrent.TrieMap

package object internal {
  object CntMediateOnce {
    def empty[T]: CntMediateOnce[T] = TrieMap.empty
  }
  type CntMediateOnce[T] = TrieMap[Container, T]

  implicit case object AccessorTypeAcceptContext extends TypedAcceptContext[Accessor[_]] {
    override def accepted: IndexedSymbol[_] => Accessor[_] => Boolean = { x => y =>
      x.isOpen || x.acceptedClass(y.t.getClass) || x.acceptedInstance(y.t)
    }
  }

  implicit case object ClassTypeAcceptContext extends TypedAcceptContext[Class[_]] {
    override def accepted: IndexedSymbol[_] => Class[_] => Boolean = { x => y =>
      x.isOpen || x.acceptedClass(y.getClass)
    }
  }
}
