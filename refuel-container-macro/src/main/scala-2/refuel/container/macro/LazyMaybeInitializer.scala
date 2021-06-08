package refuel.container.`macro`

import refuel.container.provider.{Accessor, Lazy}
import refuel.container.{Container, DependencyPoolRef, IndexedKey}

import scala.reflect.macros.blackbox

object LazyMaybeInitializer {
  def init(c: blackbox.Context)(tpe: c.Type): c.Expr[Lazy[Option[_]]] = {
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
  )(tpe: c.Type, cnt: c.Expr[Container], access: c.Expr[Accessor[_]]): c.Expr[Option[_]] = {
    import c.universe._
    val key = IndexedKey.fromType(c)(tpe)

    val exists = c
      .Expr[Option[Any]](
        q"""
        ${cnt}.find[$tpe, refuel.container.provider.Accessor[_]](${key}, ${access})
       """
      )

    val candidates = StaticDependencyExtractor.searchInjectionCandidates(c)(tpe)
    DependencyRankings(c)(candidates).fold(
      exists
    ) {
      case (_, ranked) if ranked.size > 1 =>
        c.abort(
          c.enclosingPosition,
          s"Invalid dependency definition. There must be one automatic injection per priority. But found [${ranked.map(_.fullName).mkString(", ")}]"
        )
      case (priority, ranked) =>
        val rankedOne = DependencyRankings.generateExprOne(c)(ranked.head)
        reify[Option[Any]](
          exists.splice orElse {
            key.splice.synchronized {
              exists.splice orElse {
                Some(
                  c.Expr[Option[Any]](
                      q"""${cnt}.createIndexer[${tpe}](${key}, ${rankedOne}, ${priority}).indexing().value"""
                    )
                    .splice
                )
              }
            }
          }
        )
    }
  }
}
