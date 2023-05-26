package refuel.container.`macro`

import refuel.container.Config.AdditionalPackage
import refuel.container.{Config, Container}
import refuel.inject.{AutoInject, InjectableSymbolHandler}

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

object StaticDependencyExtractor {
  private[this] var buffer: Option[Set[_]] = None

  /**
    * Returns a list of dependencies found at compile time.
    * If a @RecognizedDynamicInjection is found in the search property,
    * I would expect runtime to be ranked together.
    * Otherwise, terminate the compilation or respond to the found candidates.
    *
    * @param c context
    * @tparam C Context type
    * @tparam T Search property
    * @return [[ExcludingRuntime]] or [[ConfirmedCands]]
    */
  def searchInjectionCandidates(c: blackbox.Context)(tpe: c.Type): Vector[c.Symbol] = {
    val x = getList(c)
    InjectableSymbolHandler.filterSymbols(c)(x)(tpe)
  }

  def unloadPackages(c: blackbox.Context): Seq[String] = {
    val config = Config.blackList
    config.collect {
      case AdditionalPackage(p) => p
    } match {
      case x if x.nonEmpty =>
        c.echo(c.universe.EmptyTree.pos, s"\nUnscanning injection packages:\n    ${x.mkString("\n    ")}\n\n")
      case _ =>
    }
    config.map(_.value)
  }

  def run(c: blackbox.Context): Set[c.universe.Symbol] = {

    @tailrec
    def nealyPackage(current: c.universe.Symbol, prevs: c.universe.Symbol*): c.universe.Symbol = {
      current.owner match {
        case x if x.isPackage && x.fullName == "<root>" => x
        case x                                          => nealyPackage(x, prevs.+:(x): _*)
      }
    }
    @tailrec
    def recursivePackageExplore(
        selfPackages: Vector[c.universe.Symbol],
        injectableSymbols: Set[c.universe.Symbol] = Set.empty
    ): Set[c.Symbol] = {

      @tailrec
      def recursiveModuleExplore(
          n: Vector[c.universe.Symbol],
          injectableSymbols: Set[c.universe.Symbol] = Set.empty
      ): Set[c.Symbol] = {
        n.filter(brokenCheck(c)) match {
          case accessibleSymbol if accessibleSymbol.isEmpty => injectableSymbols
          case accessibleSymbol =>
            accessibleSymbol.filter {
              case x if x.isModule && x.typeSignature.<:<(AutoInjectionTag(c)) => true
              case x if isInjectableOnce(c)(x)                                 => true
              case _                                                           => false
            } match {
              case x =>
                recursiveModuleExplore(
                  accessibleSymbol.withFilter(_.isModule).flatMap(_.typeSignature.members).collect {
                    case r if r.isModule || isInjectableOnce(c)(r) => r
                  },
                  injectableSymbols ++ x
                )
            }
        }
      }

      selfPackages match {
        case x if x.isEmpty => injectableSymbols
        case _ =>
          val (packages, modules) = selfPackages.flatMap(_.typeSignature.decls).distinct.collect {
            case x if selfPackages.contains(x) || unloadPackages(c).contains(x.fullName) =>
              None -> None
            case x if x.isPackage =>
              Some(x) -> None
            case x
                if !x.name.toString
                  .contains("$") && ((x.isModule && !x.isAbstract) || x.isModuleClass || isInjectableOnce(c)(x)) =>
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

    val x: c.Symbol = nealyPackage(c.weakTypeOf[Container].typeSymbol)
    recursivePackageExplore(Vector(x))
  }

  private[this] def getList(c: blackbox.Context): Set[c.Symbol] = {
    buffer match {
      case None =>
        run(c) match {
          case x =>
            buffer = Some(x)
            x
        }
      case Some(x) => x.asInstanceOf[Set[c.Symbol]]
    }
  }

  private[this] def AutoInjectionTag(c: blackbox.Context) = c.weakTypeOf[AutoInject]

  @tailrec
  private[this] final def recursiveStubTesting(c: blackbox.Context)(x: c.Type*): Boolean = {
    import c.universe._
    val prts = x.collect {
      case api: ClassInfoTypeApi => api.parents
      case typeRef: TypeRef =>
        typeRef.typeSymbol.typeSignature match {
          case _api: ClassInfoTypeApi => _api.parents
          case PolyType(_, b)         => Seq(b)
        }
    }.flatten
    if (prts.isEmpty) {
      true
    } else if (!prts.exists { pr =>
                 pr.typeSymbol.isClass && pr.typeSymbol.asClass
                   .isInstanceOf[scala.reflect.internal.Symbols#StubClassSymbol]
               }) {
      recursiveStubTesting(c)(prts: _*)
    } else {
      false
    }

  }

  private[this] def isInjectableOnce(c: blackbox.Context)(x: c.Symbol): Boolean = {
    scala.util
      .Try {
        recursiveStubTesting(c)(x.typeSignature) &&
        x.isClass &&
        !x.isAbstract &&
        brokenCheck(c)(x) &&
        x.asClass.primaryConstructor.isMethod &&
        x.typeSignature.baseClasses.contains(AutoInjectionTag(c).typeSymbol)
      }
      .getOrElse(false)
  }

  private[this] def brokenCheck(c: blackbox.Context)(value: c.Symbol): Boolean = {
    try {
      if (value.isModule) {
        c.typecheck(
          c.parse(
            value.fullName.replaceAll("(.+)\\$(.+)", "$1#$2")
          ),
          silent = true
        )
          .nonEmpty
      } else {
        val params = value.typeSignature.typeParams.map { x =>
          x.typeSignature.typeParams
        }
        c.typecheck(
            tree = c.parse(
              s"type `${c.freshName()}`${
                val args = params.zipWithIndex.map {
                  case (p, i) if p.isEmpty => s"T$i"
                  case (p, i) => s"T$i[${p.map(_ => "_").mkString(",")}]"
                }.mkString(",")
                if (args.isEmpty) "" else s"[$args]"
              } = ${value.fullName.replaceAll("(.+)\\$(.+)", "$1.$2")}${
                val binds = params.zipWithIndex.map { case (_, i) => s"T$i" }.mkString(",")
                if (binds.isEmpty) "" else s"[${binds}]"
              }"
            ),
            silent = true
          )
          .nonEmpty
      }
    } catch {
      case _: Throwable => false
    }
  }

}
