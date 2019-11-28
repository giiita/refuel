package refuel.container

import scala.reflect.runtime.universe._

case class ContainerIndexedKey(value: String)
object ContainerIndexedKey {
  def apply(value: Type): ContainerIndexedKey = ContainerIndexedKey(value.toString)
}
