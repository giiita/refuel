package refuel

import refuel.container.provider.restriction.SymbolRestriction

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

package object container {
  type ContainerPool = TrieMap[Symbol, mutable.HashSet[SymbolRestriction[_]]]
}
