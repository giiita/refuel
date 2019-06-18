package com.github.giiita.`macro`

import com.github.giiita.provider.Lazy

import scala.reflect.macros.blackbox

object Macro {

  def inject[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Lazy[T]] = {
    import c.universe._

    val depections = new DetectionExtractor[c.type](c).run[T]()


    val expr = depections.map(name => q"${c.parse(name.fullName)}").toList
    val res = q"{..$expr}"
    val r = c.Expr(res)

    val weakTypeTag = weakTypeOf[T]

    c.Expr[Lazy[T]](
      q"""
         $r
         new com.github.giiita.provider.Lazy[$weakTypeTag] {
           def provide: $weakTypeTag = com.github.giiita.container.Container.get[$weakTypeTag]()
         }
        """)

  }

  def pickup_impl[T: c.WeakTypeTag](c: blackbox.Context): c.Tree = {
    import c.universe._
    q"""
       implicitly[T]
     """
  }

  def export_impl[T: c.WeakTypeTag](c: blackbox.Context)(value: c.Tree): c.Tree = {
    import c.universe._


    //    val expr = objFullNames.map(name => q"${c.parse(name)}.##").toList
    //    val res = q"{..$expr}"
    //    c.Expr(res)

    //      q"""
    //         implicit val t: ${weakTypeOf[T]} =
    //        """

    q"""
       implicit val t: ${weakTypeOf[T]} = $value
       println("xxxxxx")
     """


  }
}
