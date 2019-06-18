package com.github.giiita.`macro`

import com.github.giiita.injector.AutoInjectable

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

class DetectionExtractor[C <: blackbox.Context](c: C) {

  import c.universe._

  def run[T: C#WeakTypeTag](): Vector[C#Symbol] = {
    val nealyPackage = c.weakTypeOf[T].typeSymbol.owner.owner
    recursivePackageExplore[T](
      Vector(nealyPackage),
      Seq(
        c.symbolOf[AutoInjectable],
        c.symbolOf[T]
      )
    )
  }

  @tailrec
  private final def recursivePackageExplore[T: C#WeakTypeTag](selfPackages: Vector[Symbol],
                                                              requiredSymbol: Seq[Symbol],
                                                              result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    selfPackages match {
      case x if x.isEmpty => result
      case _ =>
        val (packages, modules) = selfPackages.flatMap(_.info.members).collect {
          case x if x.isPackage => Some(x) -> None
          case x if x.isModule => None -> Some(x)
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursivePackageExplore[T](
          packages,
          requiredSymbol,
          result ++ recursiveModuleExplore[T](modules, requiredSymbol)
        )
    }
  }

  @tailrec
  private final def recursiveModuleExplore[T: C#WeakTypeTag](n: Vector[Symbol],
                                                             requiredSymbol: Seq[Symbol],
                                                             result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    n match {
      case x if x.isEmpty => result
      case _ =>
        val (modules, targets) = n.flatMap(_.typeSignature.members).collect {
          case x if x.isModule =>
            (x, if (requiredSymbol.forall(x.typeSignature.baseClasses.contains)) Some(x) else None)
        } match {
          case x => x.map(_._1) -> x.flatMap(_._2)
        }

        recursiveModuleExplore(modules, requiredSymbol, result ++ targets)
    }
  }
}
