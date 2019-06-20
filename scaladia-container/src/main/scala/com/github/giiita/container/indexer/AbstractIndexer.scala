package com.github.giiita.container.indexer

import com.github.giiita.`macro`.Macro

abstract class AbstractIndexer[T] extends Indexer[T] {
  def scratch: Indexer[T] = macro Macro.scratch
}
