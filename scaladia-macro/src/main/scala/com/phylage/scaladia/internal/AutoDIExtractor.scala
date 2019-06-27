package com.phylage.scaladia.internal

import com.phylage.scaladia.exception.InjectDefinitionException
import com.phylage.scaladia.injector.AutoInjectable
import com.phylage.scaladia.provider.Tag
import com.typesafe.scalalogging.Logger

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

object AutoDIExtractor {
  val log: Logger = Logger(classOf[AutoDIExtractor[_]])
}

class AutoDIExtractor[C <: blackbox.Context](c: C) {

  import c.universe._

  def run[T: C#WeakTypeTag](): Vector[C#Symbol] = {

    val p = nealyPackage(c.weakTypeOf[T].typeSymbol)
    recursivePackageExplore(
      Vector(p),
      Seq(
        weakTypeOf[AutoInjectable],
        weakTypeOf[T]
      )
    ) match {
      case r =>
        AutoDIExtractor.log.info(s"With the injection of ${weakTypeOf[T]}, flush these : ${r.map(_.name).mkString(",")}")
        r
    }
  }

  @tailrec
  private final def nealyPackage(current: Symbol, prevs: Symbol*): Symbol = {
    current.owner match {
      case x if x.isPackage &&
        x.fullName == "<root>" => prevs.tail.headOption.getOrElse {
        throw new InjectDefinitionException("Autoloading is limited to interfaces that are more deeply defined than two packages. \nThe auto-read range for the \"com.phylage.scaladia.injector.Xxx\" interface is at least `com.github` objects.")
      }
      case x => nealyPackage(x, prevs.+:(x): _*)
    }
  }

  @tailrec
  private final def recursivePackageExplore(selfPackages: Vector[Symbol],
                                            requiredSymbol: Seq[Type],
                                            result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    selfPackages match {
      case x if x.isEmpty => result
      case _ =>
        val (packages, modules) = selfPackages.flatMap(_.info.members).collect {
          case x if x.isPackage =>
            Some(x) -> None
          case x if x.isModule =>
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
                                           requiredSymbol: Seq[Type],
                                           result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    n match {
      case x if x.isEmpty => result
      case _ =>
        val validated = n.collect {
          case x if allMeetCondition(requiredSymbol, x) => x
        }

        val nested = n.flatMap(_.typeSignature.members).collect {
          case x if x.isModule => x
        }

        recursiveModuleExplore(nested, requiredSymbol, result ++ validated)
    }
  }

  private def allMeetCondition(requiredSymbol: Seq[Type], module: Symbol): Boolean = {
    requiredSymbol.forall(module.typeSignature.<:<) && ! {
      module.typeSignature.baseClasses.contains(weakTypeOf[Tag[_]].typeSymbol) &&
        !requiredSymbol.exists(_.<:<(weakTypeOf[Tag[_]]))
    }
  }
}
