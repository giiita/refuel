package refuel.container

import refuel.inject.Types.LocalizedContainer

trait ContainerLifecycle {
  def container: LocalizedContainer
}
