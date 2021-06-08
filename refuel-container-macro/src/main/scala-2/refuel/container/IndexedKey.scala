package refuel.container

import scala.reflect.macros.blackbox

object IndexedKey {
  def from[T](c: blackbox.Context)(implicit tpe: c.WeakTypeTag[T]): c.Expr[scala.Symbol] = {
    import c.universe._
    c.Expr[scala.Symbol](
      q"""
      scala.Symbol(${weakTypeOf[T].toString})
     """
    )
  }
  def fromType(c: blackbox.Context)(tpe: c.Type): c.Expr[scala.Symbol] = {
    import c.universe._
    c.Expr[scala.Symbol](
      q"""
      scala.Symbol(${tpe.toString})
     """
    )
  }
  import scala.reflect.runtime.universe._
  def runtime(tpe: Type): scala.Symbol = {
    scala.Symbol(tpe.toString)
  }
}
