package refuel

import refuel.container.Container
import refuel.provider.Tag

object Types {
  type @@[+T, +U] = T with Tag[U]

  sealed trait Localized

  type LocalizedContainer = Container @@ Types.Localized
}
