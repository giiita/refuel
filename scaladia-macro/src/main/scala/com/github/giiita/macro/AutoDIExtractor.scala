package com.github.giiita.`macro`

import com.github.giiita.exception.InjectDefinitionException
import com.github.giiita.injector.AutoInjectable

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

class AutoDIExtractor[C <: blackbox.Context](c: C) {

  import c.universe._

  def run[T: C#WeakTypeTag](): Vector[C#Symbol] = {
    val p = nealyPackage(c.weakTypeOf[T].typeSymbol)
    recursivePackageExplore(
      Vector(p),
      Seq(
        c.symbolOf[AutoInjectable],
        c.symbolOf[T]
      )
    )
  }

  @tailrec
  private final def nealyPackage(current: Symbol, prevs: Symbol*): Symbol = {
    current.owner match {
      case x if x.isPackage &&
        x.fullName == "<root>" => prevs.tail.headOption.getOrElse {
        throw new InjectDefinitionException("Autoloading is limited to interfaces that are more deeply defined than two packages. \nThe auto-read range for the \"com.github.giiita.injector.Xxx\" interface is at least `com.github` objects.")
      }
      case x                   => nealyPackage(x, prevs.+:(x): _*)
    }
  }

  @tailrec
  private final def recursivePackageExplore(selfPackages: Vector[Symbol],
                                            requiredSymbol: Seq[Symbol],
                                            result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    selfPackages match {
      case x if x.isEmpty => result
      case _              =>
        val (packages, modules) = selfPackages.flatMap(_.info.members).collect {
          case x if x.isPackage =>
            Some(x) -> None
          case x if x.isModule  =>
            None -> Some(x)
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursivePackageExplore(
          packages,
          requiredSymbol,
          result ++ recursiveModuleExplore(modules, requiredSymbol)
        )
    }
  }

  @tailrec
  private final def recursiveModuleExplore(n: Vector[Symbol],
                                           requiredSymbol: Seq[Symbol],
                                           result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    n match {
      case x if x.isEmpty => result
      case _              =>
        val (modules, targets) = n.map { x =>
          (None, if (requiredSymbol.forall(x.typeSignature.baseClasses.contains)) Some(x) else None)
        } ++ n.flatMap(_.typeSignature.members).collect {
          case x if x.isModule =>
            (Some(x), if (requiredSymbol.forall(x.typeSignature.baseClasses.contains)) Some(x) else None)
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursiveModuleExplore(modules, requiredSymbol, result ++ targets)
    }
  }
}
