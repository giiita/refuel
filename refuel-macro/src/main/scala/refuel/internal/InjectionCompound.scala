package refuel.internal

import refuel.container.{CanBeContainer, Container}
import refuel.domination.InjectionPriority.Default
import refuel.domination.{Inject, InjectionPriority, PriorityCtx}
import refuel.exception.InjectDefinitionException

import scala.reflect.macros.blackbox

class InjectionCompound[C <: blackbox.Context](val c: C) {

  /* Injection priority config type tag */
  private[this] lazy val InjectionPriorityConfigType = c.weakTypeOf[Inject]

  import c.universe._

  private[this] type PrimaryDep = (c.Expr[InjectionPriority], c.Symbol)

  def buildOne[T: c.WeakTypeTag](ctn: c.Tree)(msList: Iterable[c.Symbol], mayBeCs: Iterable[c.Symbol]): c.Expr[T] = {

    (msList, mayBeCs) match {
      case (x, y) if x.isEmpty && y.isEmpty =>
        c.error(c.enclosingPosition, s"Cannot found automatic injection target of ${weakTypeOf[T].typeSymbol.fullName}.")
        c.abort(c.enclosingPosition, s"Automatic injection target can not be found.")
      case (x, y) if x.isEmpty =>
        c.echo(c.enclosingPosition, s"Actual candidates ${y.map(_.name).mkString(",")}.")
        createNewInstance[T](ctn)(resolvePriorityDeps(y.toSeq))
      case (x, _) =>
        c.echo(c.enclosingPosition, s"Actual candidates [${x.map(_.name).mkString(", ")}]")
        createModuleRef[T](ctn)(resolvePriorityDeps(x.toSeq))
    }
  }

  private[this] def resolvePriorityDeps(ss: Seq[c.Symbol]): Seq[PrimaryDep] = {
    ss.map { sm =>
      val ip: c.Expr[InjectionPriority] = sm.annotations.find(_.tree.tpe =:= InjectionPriorityConfigType)
        .flatMap(_.tree.children.tail.headOption)
        .fold[c.Expr[InjectionPriority]](reify(Default)) {
          case x if x.symbol.isModule =>
            c.Expr[InjectionPriority](q"${x.symbol.asModule.fullName}")
          case _ => throw new InjectDefinitionException(s"The injection priority setting must be a static module. From [ ${sm.fullName} ]")
        }
      ip -> sm
    }
  }

  private[this] def createModuleRef[T: c.WeakTypeTag](ctn: c.Tree)(volumes: Seq[PrimaryDep]): c.Expr[T] = {
    val result = volumes.map {
      case (exprPriority, sym) =>
        reify[PriorityCtx[T]] {
          val _cnt = c.Expr[Container](ctn).splice
          PriorityCtx(
            exprPriority.splice,
            { p: InjectionPriority =>
              val inst = c.Expr[T](c.parse(sym.fullName)).splice
              inst match {
                case x: CanBeContainer[Container] => x._cntMutation = _cnt
                case _ =>
              }
              _cnt.createIndexer(inst, p).indexing().value
            }
          )
        }
    }

    rankingApply(result)
  }

  private[this] def createNewInstance[T: c.WeakTypeTag](ctn: c.Tree)(volumes: Seq[PrimaryDep]): c.Expr[T] = {

    val result = volumes.map {
      case (exprPriority, sym) =>
        val const: c.Expr[T with CanBeContainer[Container]] = c.Expr[T with CanBeContainer[Container]] {
          c.parse(
            s"""new ${sym.fullName}(${
              sym.asClass.primaryConstructor.asMethod.paramLists.map { curry =>
                curry.map { param =>
                  s"bind[${param.typeSignature.toString}]"
                }.mkString(",")
              }.mkString(")(")
            }) with refuel.injector.Injector"""
          )
        }

        reify[PriorityCtx[T]](
          PriorityCtx(
            exprPriority.splice,
            { p: InjectionPriority =>
              val _cnt = c.Expr[Container](ctn).splice
              val inst = const.splice
              inst._cntMutation = _cnt
              _cnt.createIndexer(inst, p).indexing().value
            }
          )
        )
    }
    rankingApply(result)
  }

  private[this] def rankingApply[T: c.WeakTypeTag](v: Seq[c.Expr[PriorityCtx[T]]]) = {
    reify {
      c.Expr[Seq[PriorityCtx[T]]](q"""Seq(..$v)""").splice
        .groupBy(_.priority)
        .toSeq
        .sortBy(_._1)(InjectionPriority.Order)
        .head._2 match {
        case syms if syms.size == 1 => syms.head.f(syms.head.priority)
        case syms =>
          val cnds = syms.map(x => x.f(x.priority))
          throw new InjectDefinitionException(
            s"Invalid dependency definition of ${c.Expr(c.reifyRuntimeClass(weakTypeOf[T])).splice}. There must be one automatic injection of inject[T] per priority. But found [${cnds.mkString(", ")}]"
          )
      }
    }
  }
}
