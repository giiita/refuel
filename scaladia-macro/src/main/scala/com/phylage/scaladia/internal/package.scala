package com.phylage.scaladia

import com.phylage.scaladia.container.Container

import scala.collection.concurrent.TrieMap

package object internal {
  object CntMediateOnce {
    def empty[T]: CntMediateOnce[T] = TrieMap.empty
  }
  type CntMediateOnce[T] = TrieMap[Container, T]
}
