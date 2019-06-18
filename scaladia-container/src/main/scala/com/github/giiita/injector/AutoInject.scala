package com.github.giiita.injector

import com.github.giiita.container.Container

trait AutoInject[T] extends AutoInjectable with Injector { me: T =>
  def exportDIC(): Unit = exportDICindirect()
  private[this] def exportDICindirect(): Unit = Container.flush[T](me)
}
