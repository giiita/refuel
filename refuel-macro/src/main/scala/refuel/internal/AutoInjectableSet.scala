package refuel.internal

import refuel.exception.InjectDefinitionException
import refuel.injector.AutoInjectable

import scala.reflect.macros.blackbox

class AutoInjectableSet[C <: blackbox.Context](val c: C) {

  def filterModuleSymbols[T: c.WeakTypeTag](symbols: AutoInjectableSymbols[c.type]): Vector[c.Symbol] = {
    symbols.modules.filter { x =>
      x.typeSignature.<:<(c.weakTypeOf[AutoInjectable[T]])
    }.asInstanceOf[Set[c.Symbol]].toVector.sortBy[String](_.fullName)
  }

  def findClassSymbol[T: c.WeakTypeTag](symbols: AutoInjectableSymbols[c.type]): Option[c.Symbol] = {
    symbols.classes.filter { x =>
      x.asClass.toType.<:<(c.weakTypeOf[AutoInjectable[T]])
    } match {
      case x if x.size > 1 =>
        c.error(c.enclosingPosition, s"You cannot define multiple classes that can be InjectOnce[T]. It can be avoided by attaching a tag. [${x.map(_.name).mkString(", ")}].")
        throw new InjectDefinitionException(s"Automatic injection target can not be found.")
      case x               => x.headOption.asInstanceOf[Option[c.Symbol]]
    }
  }

}