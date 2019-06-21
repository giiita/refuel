package com.github.giiita

import com.github.giiita.internal.Macro
import com.github.giiita.container.{Container, ContainerStore}

package object injector {

  private[giiita] implicit object IndirectContainerStore extends ContainerStore {
    lazy val ctn: Container = initContainer

    def initContainer: Container = macro Macro.containerSetting[Container]
  }
}
