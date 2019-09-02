package com.phylage.scaladia.internal

import com.phylage.scaladia.container.{Container, CanBeContainer}
import com.phylage.scaladia.exception.InjectDefinitionException
import com.phylage.scaladia.injector.AutoInjectable

import scala.reflect.macros.blackbox

class InjectionCompound[C <: blackbox.Context](val c: C) {

  import c.universe._

  def buildOne[T: c.WeakTypeTag](ctn: c.Tree)(msList: Iterable[c.Symbol], mayBeCs: Option[c.Symbol]): c.Expr[T] = {

    (msList, mayBeCs) match {
      case (x, None) if x.isEmpty =>
        c.error(c.enclosingPosition, s"Cannot found automatic injection target of ${weakTypeOf[T].typeSymbol.fullName}.")
        throw new InjectDefinitionException(s"Automatic injection target can not be found.")
      case (x, Some(cs)) if x.isEmpty =>
        c.echo(c.enclosingPosition, s"Actual candidates ${cs.name}.")
        createNewInstance(ctn)(cs)
      case (x, _) =>
        c.echo(c.enclosingPosition, s"Actual candidates [${x.map(_.name).mkString(", ")}]")
        val flushed = x.map { name =>
          c.Expr[AutoInjectable[T]](
            q"""${c.parse(name.fullName)}"""
          )
        }
        reify {
          c.Expr[Iterable[AutoInjectable[T]]](
            q"Vector(..$flushed)"
          ).splice.toVector.sortBy(_.injectionPriority)(Ordering.Int.reverse).head.asInstanceOf[T]
        }
    }
  }

  private[this] def createNewInstance[T: c.WeakTypeTag](ctn: c.Tree)(s: c.Symbol): c.Expr[T] = {

    val constructInjection = s.asClass.primaryConstructor.asMethod.paramLists.map { curry =>
      s"""(${
        curry.map { param =>
          s"inject[${param.typeSignature.toString}]"
        }.mkString(",")
      })""".stripMargin

    }.mkString

    reify {
      c.Expr[T with CanBeContainer[Container]] {
        c.parse(
          s"""
             |new ${s.fullName}$constructInjection with com.phylage.scaladia.injector.Injector
             """.stripMargin
        )
      }.splice match {
        case x =>
          x._cntMutation = c.Expr[Container](ctn).splice
          x
      }
    }
  }
}
