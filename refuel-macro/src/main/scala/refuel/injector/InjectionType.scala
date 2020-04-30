package refuel.injector

import refuel.container.Container
import refuel.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe._

object InjectionType {

  def apply[T](applyment: Container => IndexedSymbol[T], name: String)(implicit wtt: WeakTypeTag[T]): InjectionType[T] = InjectionType(wtt, applyment, name)
}
case class InjectionType[T](wtt: WeakTypeTag[T], applyment: Container => IndexedSymbol[T], name: String) {
  def =:=[C](compare: InjectionType[C]): Boolean = name == compare.name
}
