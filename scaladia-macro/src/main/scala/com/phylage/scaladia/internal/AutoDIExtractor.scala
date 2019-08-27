package com.phylage.scaladia.internal

import com.phylage.scaladia.Config
import com.phylage.scaladia.Config.AdditionalPackage
import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.AutoInjectable

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

object AutoDIExtractor {
  private[this] var buffer: Option[AutoInjectableSymbols[_]] = None

  def collectApplyTarget[C <: blackbox.Context, T: c.WeakTypeTag](c: C)(ctn: c.Tree): c.Expr[T] = {

    val richSets = new AutoInjectableSet[c.type](c)

    getList[C, T](c) match {
      case x => new InjectionCompound[c.type](c).buildOne(ctn)(
        richSets.filterModuleSymbols[T](x),
        richSets.findClassSymbol[T](x)
      )
    }
  }

  private[this] def getList[C <: blackbox.Context, T: c.WeakTypeTag](c: C): AutoInjectableSymbols[c.type] = {
    buffer match {
      case None    => new AutoDIExtractor[c.type](c).run() match {
        case x =>
          buffer = Some(x)
          x
      }
      case Some(x) => x.asInstanceOf[AutoInjectableSymbols[c.type]]
    }
  }
}

class AutoDIExtractor[C <: blackbox.Context](val c: C) {

  import c.universe._

  lazy val unloadPackages: Seq[String] = {
    val config = Config.blackList
    config.collect {
      case AdditionalPackage(p) => p
    } match {
      case x if x.nonEmpty =>
        c.echo(EmptyTree.pos, s"\nUnscanning injection packages:\n    ${x.mkString("\n    ")}\n\n")
      case _               =>
    }
    config.map(_.value)
  }

  private[this] val autoInjectableTag = weakTypeOf[AutoInjectable[_]]


  def run(): AutoInjectableSymbols[c.type] = {
    recursivePackageExplore(
      Vector(nealyPackage(c.weakTypeOf[Container].typeSymbol))
    )
  }

  @tailrec
  private final def nealyPackage(current: Symbol, prevs: Symbol*): Symbol = {
    current.owner match {
      case x if x.isPackage && x.fullName == "<root>" => x
      case x                                          => nealyPackage(x, prevs.+:(x): _*)
    }
  }

  @tailrec
  private final def recursivePackageExplore(selfPackages: Vector[Symbol],
                                            injectableSymbols: AutoInjectableSymbols[c.type] = AutoInjectableSymbols.empty[c.type](c)): AutoInjectableSymbols[c.type] = {
    selfPackages match {
      case x if x.isEmpty => injectableSymbols
      case _              =>
        val (packages, modules) = selfPackages.flatMap(_.typeSignature.decls).distinct.collect {
          case x if selfPackages.contains(x) || unloadPackages.contains(x.fullName) =>
            None -> None
          case x if x.isPackage                                                     =>
            Some(x) -> None
          case x if (x.isModule && !x.isAbstract) || x.isInjectableOnce             =>
            None -> Some(x)
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursivePackageExplore(
          packages,
          injectableSymbols ++ recursiveModuleExplore(modules)
        )

    }
  }

  @tailrec
  private final def recursiveModuleExplore(n: Vector[c.Symbol],
                                           injectableSymbols: AutoInjectableSymbols[c.type] = AutoInjectableSymbols.empty[c.type](c))
  : AutoInjectableSymbols[c.type] = {
    n.accessible match {
      case accessibleSymbol if accessibleSymbol.isEmpty => injectableSymbols
      case accessibleSymbol                             =>
        accessibleSymbol.collect {
          case x if x.isModule && x.typeSignature.<:<(autoInjectableTag) => Some(x) -> None
          case x if x.isInjectableOnce                                   => None -> Some(x)
          case _                                                         => None -> None
        } match {
          case x =>
            recursiveModuleExplore(
              accessibleSymbol.withFilter(_.isModule).flatMap(_.typeSignature.members).collect {
                case r if r.isModule || r.isInjectableOnce => r
              },
              injectableSymbols.add(x.flatMap(_._1), x.flatMap(_._2))
            )
        }


    }
  }

  implicit class RichSymbol(v: c.Symbol) {
    def isInjectableOnce: Boolean = v match {
      case x => x.isClass &&
        !x.isAbstract &&
        x.asClass.primaryConstructor.isMethod &&
        x.typeSignature.baseClasses.contains(autoInjectableTag.typeSymbol)
    }
  }

  implicit class RichVectorSymbol(value: Vector[c.Symbol]) {
    def accessible: Vector[c.Symbol] = {
      value.flatMap {
        case x if x.toString.endsWith("package$") => None
        case x                                    => try {
          c.typecheck(
            c.parse(
              x.fullName.replaceAll("([\\w]+)\\$([\\w]+)", "$1#$2")
            ),
            silent = true
          ).symbol match {
            case _ => Some(x)
          }
        } catch {
          case _: Throwable => None
        }
      }
    }
  }

}
