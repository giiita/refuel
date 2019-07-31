package com.phylage.scaladia.runtime

import com.phylage.scaladia.Config
import com.phylage.scaladia.injector.AutoInjectable

import scala.annotation.tailrec
import scala.reflect.runtime.universe._


class RuntimeAutoDIExtractor {

  lazy val unloadablePackages: Seq[String] = Config.blackList.map(_.value)

  private[this] val autoDITag = weakTypeOf[AutoInjectable[_]]


  def run[T: WeakTypeTag](): Vector[Symbol] = {
    recursivePackageExplore(
      Vector(nealyPackage(weakTypeOf[T].typeSymbol))
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
                                            result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    selfPackages match {
      case x if x.isEmpty => result
      case _              =>
        val (packages, modules) = selfPackages.flatMap(_.typeSignature.decls).distinct.collect {
          case x if selfPackages.contains(x) || unloadablePackages.contains(x.fullName) =>
            None -> None
          case x if x.isPackage                                                         =>
            Some(x) -> None
          case x if x.isModule && !x.isAbstract                                         =>
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
  private final def recursiveModuleExplore(n: Vector[Symbol],
                                           result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
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
