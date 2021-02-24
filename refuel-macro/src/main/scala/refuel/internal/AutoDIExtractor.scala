package refuel.internal

import refuel.Config
import refuel.Config.AdditionalPackage
import refuel.container.Container
import refuel.container.anno.RecognizedDynamicInjection
import refuel.injector.AutoInject
import refuel.internal.di.{ConfirmedCands, ExcludingRuntime, InjectionCands}

import scala.annotation.tailrec
import scala.reflect.macros.blackbox

object AutoDIExtractor {
  private[this] var buffer: Option[AutoInjectableSymbols[_]] = None

  def all[C <: blackbox.Context]: InjectionCands[C] = {
    getList[C]
  }

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
  def searchInjectionCands[C <: blackbox.Context, T: c.WeakTypeTag](c: C): InjectionCands[C] = {
    import c.universe._

    val runtimeClasspathInjectAcception = weakTypeOf[RecognizedDynamicInjection]

    val richSets              = new AutoInjectableSet[c.type](c)
    val x                     = getList[C](c)
    val compileTimeCandidates = richSets.filterModuleSymbols[T](x) ++ richSets.filterClassSymbol[T](x)

    val annos = weakTypeOf[T] match {
      case x: AnnotatedType => x.annotations ++ weakTypeOf[T].typeSymbol.annotations
      case _                => weakTypeOf[T].typeSymbol.annotations
    }

    if (annos.exists(_.tree.tpe.=:=(runtimeClasspathInjectAcception))) {
      // If a @RecognizedDynamicInjection had been granted
      ExcludingRuntime(c)(compileTimeCandidates)
    } else if (compileTimeCandidates.isEmpty) {
      // If a @RecognizedDynamicInjection had not been granted and no candidate is found
      c.abort(
        c.enclosingPosition,
        s"Can't find a dependency registration of ${c.weakTypeOf[T]}. Injection from runtime classpath must be given @RecognizedDynamicInjection."
      )
    } else {
      // If a @RecognizedDynamicInjection had not been granted and a candidate is found
      ConfirmedCands(c)(compileTimeCandidates)
    }
  }

  private[this] def getList[C <: blackbox.Context](c: C): AutoInjectableSymbols[c.type] = {
    buffer match {
      case None =>
        new AutoDIExtractor[c.type](c).run() match {
          case x =>
            buffer = Some(x)
            x
        }
      case Some(x) => x.asInstanceOf[AutoInjectableSymbols[c.type]]
    }
  }
}

class AutoDIExtractor[C <: blackbox.Context](val c: C) {

  import c.universe._

  private[this] final val AutoInjectionTag = c.weakTypeOf[AutoInject]

  lazy val unloadPackages: Seq[String] = {
    val config = Config.blackList
    config.collect {
      case AdditionalPackage(p) => p
    } match {
      case x if x.nonEmpty =>
        c.echo(EmptyTree.pos, s"\nUnscanning injection packages:\n    ${x.mkString("\n    ")}\n\n")
      case _ =>
    }
    config.map(_.value)
  }

  def run(): AutoInjectableSymbols[c.type] = {
    recursivePackageExplore(
      Vector(nealyPackage(c.weakTypeOf[Container].typeSymbol))
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
  private final def recursivePackageExplore(
      selfPackages: Vector[c.Symbol],
      injectableSymbols: AutoInjectableSymbols[c.type] = AutoInjectableSymbols.empty[c.type](c)
  ): AutoInjectableSymbols[c.type] = {

    selfPackages match {
      case x if x.isEmpty => injectableSymbols
      case _ =>
        val (packages, modules) = selfPackages.flatMap(_.typeSignature.decls).distinct.collect {
          case x if selfPackages.contains(x) || unloadPackages.contains(x.fullName) =>
            None -> None
          case x if x.isPackage =>
            Some(x) -> None
          case x
              if !x.name.toString
                .contains("$") && ((x.isModule && !x.isAbstract) || x.isModuleClass || x.isInjectableOnce) =>
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

  @tailrec
  private final def recursiveModuleExplore(
      n: Vector[c.Symbol],
      injectableSymbols: AutoInjectableSymbols[c.type] = AutoInjectableSymbols.empty[c.type](c)
  ): AutoInjectableSymbols[c.type] = {
    n.accessible match {
      case accessibleSymbol if accessibleSymbol.isEmpty => injectableSymbols
      case accessibleSymbol =>
        accessibleSymbol.collect {
          case x if x.isModule && x.typeSignature.<:<(AutoInjectionTag) => Some(x) -> None
          case x if x.isInjectableOnce                                  => None    -> Some(x)
          case _                                                        => None    -> None
        } match {
          case x =>
            recursiveModuleExplore(
              accessibleSymbol.withFilter(_.isModule).flatMap(_.typeSignature.members).collect {
                case r if r.isModule || r.isInjectableOnce => r
              },
              injectableSymbols.add(x.flatMap(_._1), x.flatMap(_._2))
            )
        }
    }
  }

  @tailrec
  private[this] final def recursiveStubTesting(x: c.Type*): Boolean = {
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
      recursiveStubTesting(prts: _*)
    } else {
      false
    }

  }

  implicit class RichSymbol(x: c.Symbol) {
    def isInjectableOnce: Boolean =
      scala.util
        .Try {
          recursiveStubTesting(x.typeSignature) &&
          x.isClass &&
          !x.isAbstract &&
          brokenCheck(x) &&
          x.asClass.primaryConstructor.isMethod &&
          x.typeSignature.baseClasses.contains(AutoInjectionTag.typeSymbol)
        }
        .getOrElse(false)
  }

  private[this] def brokenCheck(value: c.Symbol): Boolean = {
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
        c.typecheck(
            tree = c.parse(
              s"type `${c.freshName()}` = ${value.fullName.replaceAll("(.+)\\$(.+)", "$1.$2")}"
            ),
            silent = true
          )
          .nonEmpty
      }
    } catch {
      case _: Throwable => false
    }
  }

  implicit class RichVectorSymbol(value: Vector[c.Symbol]) {
    def accessible: Vector[c.Symbol] = {
      value.filter(brokenCheck)
    }
  }

}
