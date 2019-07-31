package com.phylage.scaladia.runtime

import com.phylage.scaladia.Config
import com.phylage.scaladia.injector.AutoInjectable

import scala.annotation.tailrec
import scala.reflect.runtime.universe


class RuntimeAutoDIExtractor {

  lazy val unloadablePackages: Seq[String] = Config.blackList.map(_.value)

  private[this] val autoDITag = universe.weakTypeOf[AutoInjectable[_]]


  def run(): Vector[universe.Symbol] = {
    recursivePackageExplore(
      Vector(nealyPackage)
    )
  }

  private final def nealyPackage: universe.Symbol = universe.runtimeMirror(getClass.getClassLoader).RootPackage.info.typeSymbol

  @tailrec
  private final def recursivePackageExplore(selfPackages: Vector[universe.Symbol],
                                            result: Vector[universe.Symbol] = Vector.empty): Vector[universe.Symbol] = {
    println("== PACKAGE")
    selfPackages.flatMap(_.typeSignature.finalResultType.members).distinct.foreach(println)

    selfPackages match {
      case x if x.isEmpty => result
      case _              =>
        val (packages, modules) = selfPackages.flatMap(_.typeSignature.finalResultType.members).distinct.collect {
          case x if selfPackages.contains(x) || unloadablePackages.contains(x.fullName) =>
            None -> None
          case x if x.isPackage                                                         =>
            Some(x) -> None
          case x if x.isModule && !x.toString.startsWith("package object") && !x.isAbstract                                         =>
            None -> Some(x)
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursivePackageExplore(
          packages,
          result ++ recursiveModuleExplore(modules)
        )
    }
  }

  @tailrec
  private final def recursiveModuleExplore(n: Vector[universe.Symbol],
                                           result: Vector[universe.Symbol] = Vector.empty): Vector[universe.Symbol] = {
    println("== MODULE")
    n.foreach(println)
    n match {
      case accessibleSymbol if accessibleSymbol.isEmpty => result
      case accessibleSymbol                             =>
        recursiveModuleExplore(
          accessibleSymbol.withFilter(_.isModule).flatMap(_.typeSignature.members).collect {
            case x if x.isModule => x
          },
          result ++ accessibleSymbol.filter(_.typeSignature.<:<(autoDITag)))
    }
  }

}
