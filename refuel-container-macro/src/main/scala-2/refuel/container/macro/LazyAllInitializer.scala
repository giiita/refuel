package refuel.container.`macro`

import refuel.container.provider.{Accessor, Lazy}
import refuel.container.{Container, DependencyPoolRef, IndexedKey}

import scala.reflect.macros.blackbox

object LazyAllInitializer {
  def init(c: blackbox.Context)(tpe: c.Type): c.Expr[Lazy[Iterable[_]]] = {
    import c.universe._
    val access = c.Expr[Accessor[_]](c.inferImplicitValue(weakTypeOf[Accessor[_]]))

    reify(
      Lazy { container =>
        maybeInitInject(c)(tpe, c.Expr[Container](q"container"), access).splice
          .map {
            case value: DependencyPoolRef[_] =>
              value.asInstanceOf[DependencyPoolRef[Container]].__refuel_cRef = Some(container)
              value
            case value => value
          }
      }
    )
  }

  def maybeInitInject(
      c: blackbox.Context
  )(tpe: c.Type, cnt: c.Expr[Container], access: c.Expr[Accessor[_]]): c.Expr[Iterable[_]] = {
    import c.universe._
    val key = IndexedKey.fromType(c)(tpe)

    val exists = c
      .Expr[Iterable[Any]](
        q"""
        ${cnt}.fully[$tpe, refuel.container.provider.Accessor[_]](${key}, ${access})
       """
      )

    val candidates = StaticDependencyExtractor.searchInjectionCandidates(c)(tpe)

    if (candidates.isEmpty) {
      exists
    } else {
      DependencyRankings(c)(candidates).fold(
        c.abort(
          c.enclosingPosition,
          s"Invalid dependency definition. There must be one automatic injection per priority."
        )
      ) {
        case (priority, ranked) =>
          val rankedAll = DependencyRankings.generateExpr(c)(ranked)
          reify[Iterable[Any]] {
            val injectFirst = exists.splice
            if (injectFirst.isEmpty) {
              key.splice.synchronized {
                val injectSecond = exists.splice
                if (injectSecond.isEmpty) {
                  rankedAll.splice.map { x =>
                    c.Expr[Any](
                        q"""${cnt}.createIndexer[${tpe}](${key}, x, ${priority}).indexing().value"""
                      )
                      .splice
                  }
                } else injectSecond
              }
            } else injectFirst
          }
      }
    }
  }
}
