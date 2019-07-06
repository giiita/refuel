package com.phylage.scaladia.internal

import com.phylage.scaladia.Config
import com.phylage.scaladia.injector.AutoInjectable
import com.phylage.scaladia.provider.Tag

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

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
      case x => AutoInjectableSet(c)(x.asInstanceOf[Vector[c.Symbol]])
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
        case x =>
          c.echo(c.enclosingPosition, s"Flash ${tag.typeSymbol.fullName}'s Actual Conditions ${x.map(_.name).mkString(",")}.")
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
    "<empty>",

    // For later scala 2.11 compilation errors
    // see https://github.com/giiita/scaladia/issues/29
    "org.scalatest",
    "org.scalatestplus",
    "akka"
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
      case x => nealyPackage(x, prevs.+:(x): _*)
    }
  }

  @tailrec
  private final def recursivePackageExplore(selfPackages: Vector[Symbol],
                                            result: Vector[Symbol] = Vector.empty): Vector[Symbol] = {
    selfPackages match {
      case x if x.isEmpty => result
      case _ =>
        val (packages, modules) = selfPackages.flatMap(_.typeSignature.decls).distinct.collect {
          case x if selfPackages.contains(x) || unloadablePackages.contains(x.fullName) =>
            None -> None
          case x if x.isPackage =>
            Some(x) -> None
          case x if x.isModule && !x.isClass && !x.isAbstract =>
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
      case accessible =>
        recursiveModuleExplore(
          accessible.flatMap(_.typeSignature.members).collect {
            case x if x.isModule && !x.isClass => x
          },
          result ++ accessible.filter(_.typeSignature.<:<(autoDITag)))
    }
  }

  implicit class RichVectorSymbol(value: Vector[Symbol]) {
    def accessible: Vector[Symbol] = {
      value.flatMap { v =>
        v match {
          case x if x.toString.endsWith("package$") => None
          case x => try {
            c.typecheck(
              c.parse(
                x.fullName.replaceAll("([\\w]+)\\$([\\w]+)", "$1#$2")
              ),
              silent = true
            ).symbol.typeSignature.members match {
              case _ => Some(x)
            }
          } catch {
            case _ => None
          }
        }
      }
    }
  }

}
