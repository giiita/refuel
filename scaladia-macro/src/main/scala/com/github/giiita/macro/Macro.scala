package com.github.giiita.`macro`

import com.github.giiita.provider.Lazy

import scala.reflect.macros.blackbox

object Macro {

  def inject[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Lazy[T]] = {
    import c.universe._

    val detections = new DetectionExtractor[c.type](c).run[T]()

    val expr = detections.map(name => q"${c.parse(name.fullName)}.flush[${weakTypeOf[T]}]").toList
    c.Expr[Lazy[T]](
      lazyInit(c)(
        c.Expr(q"{..$expr}"),
        weakTypeOf[T]
      )
    )
  }
}
