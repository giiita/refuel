package com.github.giiita

import scala.reflect.macros.blackbox

package object `macro` {
  def lazyInit[T](c: blackbox.Context)(fire: c.Expr[T], tag: c.universe.Type): c.Tree = {
    import c.universe._
    q"""
         $fire
         new com.github.giiita.provider.Lazy[$tag] {
           def provide: $tag = com.github.giiita.container.Container.get[$tag]()
         }
        """
  }
}
