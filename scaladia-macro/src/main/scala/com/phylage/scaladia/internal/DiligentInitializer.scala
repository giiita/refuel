package com.phylage.scaladia.internal

import scala.reflect.macros.blackbox

class DiligentInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def diligentInjection[T: c.WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {
    AutoDIExtractor.collectApplyTarget[c.type, T](c)(ctn)
  }
}
