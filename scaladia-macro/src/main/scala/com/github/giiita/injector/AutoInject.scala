package com.github.giiita.injector

trait AutoInject[T] extends Injector {
  com.github.giiita.container.Container.flush(this)
}
