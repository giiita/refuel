package refuel.container

import scala.quoted._

object IndexedKey {
  import scala.quoted.Type
  def from[T <: AnyKind: Type](using q: Quotes): Expr[IndexedKey] = {
    import q.reflect._
    '{
      scala.Symbol(${Expr(q.reflect.TypeRepr.of[T].show)})
    }
  }
}

type IndexedKey = Symbol
