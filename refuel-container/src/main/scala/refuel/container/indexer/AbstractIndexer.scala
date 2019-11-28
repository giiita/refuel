package refuel.container.indexer

import refuel.internal.Macro

abstract class AbstractIndexer[T] extends Indexer[T] {
  def scratch: Indexer[T] = macro Macro.scratch
}
