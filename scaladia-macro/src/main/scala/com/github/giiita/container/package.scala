package com.github.giiita

import com.github.giiita.`macro`.DetectionExtractor

import scala.reflect.macros.blackbox

package object container {
//  def pickup_impl[T: c.WeakTypeTag](c: blackbox.Context)(symbol: c.Tree): c.Tree = {
//    import c.universe._
//
//    val ll = new DetectionExtractor[c.type](c).run[T]().head
//
//
//    //    val expr = objFullNames.map(name => q"${c.parse(name)}.##").toList
//    //    val res = q"{..$expr}"
//    //    c.Expr(res)
//
//      q"""
//         implicit val t: ${weakTypeOf[T]} =
//        """
//
//  }
}
