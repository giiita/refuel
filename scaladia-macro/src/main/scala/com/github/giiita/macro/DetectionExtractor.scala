package com.github.giiita.`macro`

import com.github.giiita.injector.AutoInjectable

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

class DetectionExtractor[C <: blackbox.Context](c: C) {

  import c.universe._

  def run[T: C#WeakTypeTag](): Vector[C#Symbol] = {
    val nealyPackage = c.weakTypeOf[T].typeSymbol.owner.owner
    recursivePackageExplore(Vector(nealyPackage), c.symbolOf[AutoInjectable])
  }

  @tailrec
  private final def recursivePackageExplore(selfPackages: Vector[Symbol],
                                            autodiSymbol: Symbol,
                                            result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    selfPackages match {
      case x if x.isEmpty => result
      case _              =>
        val (packages, modules) = selfPackages.flatMap(_.info.members).collect {
          case x if x.isPackage => Some(x) -> None
          case x if x.isModule  => None -> Some(x)
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursivePackageExplore(packages, autodiSymbol, result ++ recursiveModuleExplore(modules, autodiSymbol))
    }
  }

  @tailrec
  private final def recursiveModuleExplore(n: Vector[Symbol],
                                           autodiSymbol: Symbol,
                                           result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    n match {
      case x if x.isEmpty => result
      case _              =>
        val (modules, targets) = n.flatMap(_.typeSignature.members).collect {
          case x if x.isModule => (x, if (x.typeSignature.baseClasses.contains(autodiSymbol)) Some(x) else None)
        } match {
          case x => x.map(_._1) -> x.flatMap(_._2)
        }

        recursiveModuleExplore(modules, autodiSymbol, result ++ targets)
    }
  }
}
