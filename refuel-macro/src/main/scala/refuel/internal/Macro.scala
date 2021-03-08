package refuel.internal

import refuel.container.Container
import refuel.provider.Lazy

import scala.reflect.macros.blackbox

class Macro(val c: blackbox.Context) {

  def lazyInject[T: c.WeakTypeTag](ip: c.Tree, access: c.Tree): c.Expr[Lazy[T]] = {
    import c.universe._

    val prType = weakTypeOf[T]

    if (prType.<:<(weakTypeOf[Lazy[_]])) {
      reify[Lazy[T]] {
        new Lazy[T] {
          def _provide(implicit ctn: Container): T = c.Expr[T](q"""
               inject[${prType.dealias.typeArgs.head}] match {
                 case x: refuel.injector.Injector =>
                   x._cntRef = ctn
                   x
                 case x => x
               }
               """).splice
        }
      }
    } else {
      new LazyInitializer[c.type](c).lazyInit[T](
        ip,
        access
      )
    }
  }

  def scratch: c.Tree = {
    import c.universe._
    q"""
      throw new refuel.exception.UnExceptedOperateException("If you have already authorized any instance, you can not authorize new types.")
     """
  }
}
