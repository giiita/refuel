package refuel.container.macros

import refuel.container.{Container, IndexedKey}
import refuel.container.provider.{Accessor, Lazy}
import refuel.container.macros.{LazyAllInitializer, LazyForceInitializer, LazyMaybeInitializer}
import refuel.container.indexer.Indexer
import refuel.inject.InjectionPriority

import scala.quoted._

object Macro {

  def inject[T: Type](using q: Quotes): Expr[Lazy[T]] = {
    q.reflect.TypeTree.of[T].tpe.asType match {
      case '[Lazy[t]] =>
        val resultExprAlias = '{
          Lazy[Lazy[t]](${dependentKindApplication[t]})
        }
        resultExprAlias.asInstanceOf[Expr[Lazy[T]]]
      case _ =>
        dependentKindApplication[T]
    }
  }

  def dependentKindApplication[T: Type](using q: Quotes): Expr[Lazy[T]] = {
    q.reflect.TypeTree.of[T].tpe.asType match {
      case '[Option[t]] =>
        LazyMaybeInitializer.init[t].asInstanceOf[Expr[Lazy[T]]]
      case '[Iterable[t]] =>
        LazyAllInitializer.init[t].asInstanceOf[Expr[Lazy[T]]]
      case _ =>
        LazyForceInitializer.init[T]
    }
  }

  def index[T: Type](using q: Quotes)(priority: Expr[InjectionPriority], self: Expr[T]): Expr[T] = {
    import q.reflect._
    val container: Expr[Container] = Implicits.search(q.reflect.TypeRepr.of[Container]) match {
      case iss: ImplicitSearchSuccess => iss.tree.asExpr.asInstanceOf[Expr[Container]]
      case _: ImplicitSearchFailure => report.throwError("No found container reference.")
    }

    '{
    ${container}.createIndexer[T](${IndexedKey.from[T]}, ${self}, ${priority}, Vector.empty).indexing().value
    }
  }

  def indexer[T: Type](using q: Quotes)(priority: Expr[InjectionPriority], self: Expr[T]): Expr[Indexer[T]] = {
    import q.reflect._
    val container: Expr[Container] = Implicits.search(q.reflect.TypeRepr.of[Container]) match {
      case iss: ImplicitSearchSuccess => iss.tree.asExpr.asInstanceOf[Expr[Container]]
      case _: ImplicitSearchFailure => report.throwError("No found container reference.")
    }

    '{
      ${container}.createIndexer[T](${IndexedKey.from[T]}, ${self}, ${priority}, Vector.empty)
    }
  }
}
