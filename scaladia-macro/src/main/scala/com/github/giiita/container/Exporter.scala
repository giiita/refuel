package com.github.giiita.container

import scala.reflect.macros.blackbox

object Exporter {
  def export_impl[T: c.WeakTypeTag](c: blackbox.Context)(value: c.Tree): c.Tree = {
    import c.universe._


    //    val expr = objFullNames.map(name => q"${c.parse(name)}.##").toList
    //    val res = q"{..$expr}"
    //    c.Expr(res)

    //      q"""
    //         implicit val t: ${weakTypeOf[T]} =
    //        """

    println(s"EXPO: ${c.weakTypeOf[T].typeSymbol.fullName}")

    q"""
       implicit val t: com.github.giiita.injector.scope.Scope[${weakTypeOf[T]}] = com.github.giiita.injector.scope.OpenScope($value)
       println("EXPO " + ${weakTypeOf[T].typeSymbol.fullName})
     """
  }


  def pickup_impl[T: c.WeakTypeTag](c: blackbox.Context)(): c.Tree = {
    import c.universe._
    q"""
       implicitly[T]
     """
  }

}
