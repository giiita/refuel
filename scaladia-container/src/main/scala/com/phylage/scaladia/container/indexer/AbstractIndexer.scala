package com.phylage.scaladia.container.indexer

import com.phylage.scaladia.internal.Macro

abstract class AbstractIndexer[T] extends Indexer[T] {
  def scratch: Indexer[T] = macro Macro.scratch
}
