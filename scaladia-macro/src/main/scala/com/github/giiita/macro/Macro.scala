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
}
