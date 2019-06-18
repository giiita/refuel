package com.github.giiita.injector

trait AutoInject[T] extends AutoInjectable with Injector {
  com.github.giiita.container.Container.flush(this)
}
