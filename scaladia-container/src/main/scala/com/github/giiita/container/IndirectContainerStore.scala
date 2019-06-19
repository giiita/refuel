package com.github.giiita.container

import com.github.giiita.injector.Injector

object IndirectContainerStore extends Injector {
  lazy val ctn: Container = inject[Container]
}
