package com.phylage.scaladia

import com.phylage.scaladia.internal.Macro
import com.phylage.scaladia.container.{Container, ContainerStore, DefaultContainer}

package object injector {

  private[scaladia] implicit object IndirectContainerStore extends ContainerStore {
    lazy val ctn: Container = initContainer

    def initContainer: Container = DefaultContainer// macro Macro.containerSetting[Container]
  }
}
