package com.github.giiita.`macro`

import com.github.giiita.exception.InjectDefinitionException
import com.github.giiita.injector.AutoInjectable
import com.github.giiita.provider.Tag

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

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
        println(s"Recursive complete : ${r.map(_.name).mkString(",")}")
        r
    }
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
                                            requiredSymbol: Seq[Type],
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
                                           requiredSymbol: Seq[Type],
                                           result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    n match {
      case x if x.isEmpty => result
      case _              =>

        n.withFilter(_.isModule).foreach { x =>
          println(
            s"""${requiredSymbol.mkString(",")} => ${x} assigned is ${allMeetCondition(requiredSymbol, x)}""".stripMargin)
        }

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

    // MOMOから　MOMO A が消えるようにする
    // println(s"""Module baseclass : ${module} => ${module.typeSignature.baseClasses.contains(weakTypeOf[Tag[_]].typeSymbol)}""")
    // println(s"Required ${requiredSymbol.mkString(",")} as ${!requiredSymbol.exists(_.<:<(weakTypeOf[Tag[_]]))}")
    requiredSymbol.forall(module.typeSignature.<:<) && !{
      module.typeSignature.baseClasses.contains(weakTypeOf[Tag[_]].typeSymbol) &&
        !requiredSymbol.exists(_.<:<(weakTypeOf[Tag[_]]))
    }
  }
}
