package com.phylage.scaladia.internal

import java.io.IOException

import com.phylage.scaladia.Config
import com.phylage.scaladia.injector.AutoInjectable
import com.phylage.scaladia.provider.Tag

import scala.annotation.tailrec
import scala.reflect.macros.blackbox
import scala.util.{Failure, Success}

object AutoDIExtractor {
  private[this] var buffer: Vector[Any] = Vector.empty

  def getList[C <: blackbox.Context, T: C#WeakTypeTag](c: C): AutoInjectableSet[C] = {
    val xx = buffer match {
      case x if x.isEmpty =>
        new AutoDIExtractor(c).run[T]() match {
          case r =>
            buffer = r
            AutoInjectableSet(c)(r.asInstanceOf[Vector[c.Symbol]])
        }
      case x              => AutoInjectableSet(c)(x.asInstanceOf[Vector[c.Symbol]])
    }

    xx
  }

  case class AutoInjectableSet[C <: blackbox.Context](c: C)(value: Vector[C#Symbol]) {
    def filter[T: C#WeakTypeTag]: Vector[C#Symbol] = {
      import c.universe._
      val tag = weakTypeOf[T]
      value.filter { x =>
        x.typeSignature.<:<(tag) && ! {
          x.typeSignature.baseClasses.contains(weakTypeOf[Tag[_]].typeSymbol) &&
            !tag.<:<(weakTypeOf[Tag[_]])
        }
      } match {
        case x if x.isEmpty =>
          c.warning(c.enclosingPosition, s"${tag.typeSymbol.fullName}'s automatic injection target can not be found.")
          x
        case x              =>
          c.info(c.enclosingPosition, s"Flash ${tag.typeSymbol.fullName}'s Actual Conditions ${x.map(_.name).mkString(",")}.", force = false)
          x
      }
    }
  }

}

class AutoDIExtractor[C <: blackbox.Context](val c: C) {

  import c.universe._

  lazy val unloadablePackages: Seq[String] = Config.blackList ++ List(
    "com.oracle",
    "com.apple",
    "com.sun",
    "oracle",
    "apple",
    "scala",
    "javafx",
    "javax",
    "sun",
    "java",
    "jdk",
    "<empty>"
        ,
        // "akka",
        "com.fasterxml",
        "com.typesafe",
        "org.scalatest",
        "org.scalatools.testing"
  )

  private[this] val autoDITag = weakTypeOf[AutoInjectable]


  def run[T: C#WeakTypeTag](): Vector[Symbol] = {

    val p = nealyPackage(c.weakTypeOf[T].typeSymbol)
    recursivePackageExplore(
      Vector(p)
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
          case x if x.isModule                               =>
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
    n.accessible match {
      case x if x.isEmpty => result
      case accessible     =>
        val validated = accessible.filter(allMeetCondition)

        val nested = Vector.empty
//        val nested = accessible.flatMap(_.typeSignature.members).collect {
//          case x if x.isModule && !x.isClass => x
//        }

        recursiveModuleExplore(nested, result ++ validated)
    }
  }

  private def allMeetCondition(module: Symbol): Boolean = {
    true
    // module.typeSignature.baseClasses.contains(autoDITag.typeSymbol)
  }

  implicit class RichVectorSymbol(value: Vector[Symbol]) {
    def accessible: Vector[Symbol] = {
      value.flatMap { x =>
        try {
          if (c.typecheck(q"${c.parse(x.fullName)}", silent = true).isEmpty) {
            println(s"None $x")
            None
          } else {
            println(s"Some $x")
            Some(x)
          }
        } catch {
          case _ =>
            println(s"None $x")
            None
        }
//        } match {
//          case Success(r) =>
//            println("Success")
//            Some(x)
//          case Failure(e) =>
//            println(s"Fail ${e.getMessage}")
//            c.warning(c.enclosingPosition, e.getMessage)
//            None
//          case _          =>
//            println("Empty")
//            None
//        }
      }
    }
  }

}
