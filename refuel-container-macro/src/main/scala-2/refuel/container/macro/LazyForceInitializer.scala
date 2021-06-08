package refuel.container.`macro`

import refuel.container.provider.{Accessor, Lazy}
import refuel.container.{Container, DependencyPoolRef, IndexedKey}

import scala.reflect.macros.blackbox

object LazyForceInitializer {
  def init(c: blackbox.Context)(tpe: c.Type): c.Expr[Lazy[_]] = {
    import c.universe._
    val access = c.Expr[Accessor[_]](c.inferImplicitValue(weakTypeOf[Accessor[_]]))

    reify(
      Lazy { container =>
        val __refuel_result = forceInitInject(c)(tpe, c.Expr[Container](q"container"), access).splice
        __refuel_result match {
          case value: DependencyPoolRef[_] =>
            value.asInstanceOf[DependencyPoolRef[Container]].__refuel_cRef = Some(container)
          case _ =>
        }
        __refuel_result
      }
    ).asInstanceOf[c.Expr[Lazy[_]]]
  }

  def forceInitInject(
      c: blackbox.Context
  )(tpe: c.Type, cnt: c.Expr[Container], access: c.Expr[Accessor[_]]): c.Expr[_] = {
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
      c.abort(
        c.enclosingPosition,
        s"Can't find a dependency registration of ${tpe.typeSymbol.fullName}."
      )
    ) {
      case (_, ranked) if ranked.size > 1 =>
        c.abort(
          c.enclosingPosition,
          s"Invalid dependency definition. There must be one automatic injection per priority. But found [${ranked.map(_.fullName).mkString(", ")}]"
        )
      case (priority, ranked) =>
        val rankedOne = DependencyRankings.generateExprOne(c)(ranked.head)
        reify[Any](
          exists.splice getOrElse {
            key.splice.synchronized {
              exists.splice getOrElse {
                c.Expr[Any](
                    q"""${cnt}.createIndexer[${tpe}](${key}, ${rankedOne}, ${priority}).indexing().value"""
                  )
                  .splice
              }
            }
          }
        )
    }
  }
}
