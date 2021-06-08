package refuel.container.`macro`

import refuel.container.provider.Lazy

import scala.reflect.macros.blackbox

object Macro {
  def inject[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Lazy[T]] = {
    import c.universe._
    val wtt = c.weakTypeTag[T]
    if (wtt.tpe.<:<(weakTypeOf[Lazy[_]])) {
      reify(
        Lazy[T](_ => dependentKindApplication(c)(wtt.tpe.dealias.typeArgs.head).asInstanceOf[Expr[T]].splice)
      )
    } else {
      dependentKindApplication(c)(wtt.tpe.dealias).asInstanceOf[Expr[Lazy[T]]]
    }
  }

  def dependentKindApplication[T](c: blackbox.Context)(tpe: c.Type): c.Expr[Lazy[_]] = {
    import c.universe._
    if (tpe.<:<(weakTypeOf[Option[_]])) {
      LazyMaybeInitializer.init(c)(tpe.dealias.typeArgs.head)
    } else if (tpe.<:<(weakTypeOf[Iterable[_]])) {
      LazyAllInitializer.init(c)(tpe.dealias.typeArgs.head)
    } else {
      LazyForceInitializer.init(c)(tpe)
    }
  }
}
