package com.github.giiita.container.indexer

import com.github.giiita.internal.Macro

abstract class AbstractIndexer[T] extends Indexer[T] {
  def scratch: Indexer[T] = macro Macro.scratch
}
