package com.github.giiita.`macro`

import scala.reflect.macros.blackbox

object PickupMacro {

  def pickup_impl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[T] = {
    import c.universe._

    c.Expr[T] {
      q"""
         com.github.giiita.container.IndirectContainerStore.ctn.getBuffer.collect {
           case x if x.isSameAs[${weakTypeOf[T]}] => x
         }.sortBy(_.priority).lastOption match {
           case Some(x) => x.value.asInstanceOf[${weakTypeOf[T]}]
           case None => throw new Exception("Cannot found " + ${weakTypeOf[T].typeSymbol.fullName} + " implementation.")
         }
       """
    }
  }
}
