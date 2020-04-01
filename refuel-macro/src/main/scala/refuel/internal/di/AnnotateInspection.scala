package refuel.internal.di

import scala.reflect.macros.blackbox

case class AnnotateInspection[C <: blackbox.Context](c: C) {
  implicit class ExtensionSymbol(sym: c.Symbol) {
    def hasAnno(tpe: c.Type): Boolean = {
      sym.annotations.exists(_.tree.tpe.=:=(tpe))
    }
  }
}
