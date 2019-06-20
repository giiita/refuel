package com.github.giiita.container.indexer

import com.github.giiita.injector.scope.InjectableScope

trait Acception[T] {
  val acceptFunction: InjectableScope[T]
}
