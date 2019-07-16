package com.phylage.scaladia

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.provider.Tag

object Types {
  type @@[+T, +U] = T with Tag[U]

  sealed trait Localized
  private[scaladia] type LocalizedContainer = @@[Container, Types.Localized]
  private[scaladia] trait Globaly
}
