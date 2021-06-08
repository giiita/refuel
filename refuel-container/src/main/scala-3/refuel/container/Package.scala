package refuel.container

import refuel.container.provider.restriction.SymbolRestriction

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

type ContainerPool = TrieMap[Symbol, mutable.HashSet[SymbolRestriction[_]]]

given Container = ContainerImpl()
