package refuel.container.macros.internal

import dotty.tools.dotc.core.{Definitions, Symbols, Types}
import refuel.container.Config.AdditionalPackage
import refuel.container.macros.internal.StaticDependencyExtractor.{isBrokenSymbol, isClass, isModule}
import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias
import refuel.container.{Config, Container}
import refuel.inject.{AutoInject, InjectableSymbolHandler, InjectableTag}

import scala.annotation.tailrec
import scala.quoted._

object StaticDependencyExtractor extends LowLevelAPIConversionAlias {
  private[this] var buffer: Option[Any] = None

  /**
   * Returns a list of dependencies found at compile time.
   * If a @RecognizedDynamicInjection is found in the search property,
   * I would expect runtime to be ranked together.
   * Otherwise, terminate the compilation or respond to the found candidates.
   *
   * @return [[ExcludingRuntime]] or [[ConfirmedCands]]
   */
  def searchInjectionCandidates[T: Type](using q: Quotes): Iterable[q.reflect.TypeTree] = {
    import q.reflect._

    val x = getList
    InjectableSymbolHandler.filterTargetSymbols[T](x)
  }

  private[this] def getList(using q: Quotes): Iterable[q.reflect.TypeTree] = {
    buffer match {
      case None =>
        run match {
          case x =>
            buffer = Some(x)
            x
        }
      case Some(x) => x.asInstanceOf[Iterable[q.reflect.TypeTree]]
    }
  }

  private[this] def unloadPackages(using q: Quotes): Seq[String] = {
    val config = Config.blackList
    config.collect {
      case AdditionalPackage(p) => p
    } match {
      case x if x.nonEmpty =>
        q.reflect.report.info(s"\nUnscanning injection packages:\n    ${x.mkString("\n    ")}\n\n")
      case _ =>
    }
    config.map(_.value)
  }

  private[this] def run(using q: Quotes): Iterable[q.reflect.TypeTree] = {
    recursivePackageExplore(
      unloadPackages,
      Vector(nealyPackage(q.reflect.TypeTree.of[Container].symbol))
    )
  }

  @tailrec
  private[this] final def nealyPackage(using q: Quotes)(current: q.reflect.Symbol): q.reflect.Symbol = {
    current.owner match {
      case x if x.isPackageDef && x.name == "<root>" => x
      case x => nealyPackage(x)
    }
  }

  private[this] final def joinSymbolSet(using q: Quotes)(a: Iterable[q.reflect.TypeTree], b: Iterable[q.reflect.TypeTree]): Iterable[q.reflect.TypeTree] = {
    a ++ b
  }

  @tailrec
  private[this] final def recursivePackageExplore(using q: Quotes)(
    unloads: Seq[String],
    selfPackages: Vector[q.reflect.Symbol],
    injectableSymbols: Iterable[q.reflect.TypeTree] = None
  ): Iterable[q.reflect.TypeTree] = {
//    println("\n \n")
    selfPackages.distinct match {
      case x if x.isEmpty => injectableSymbols
      case _ =>
        val (packages, modules) = selfPackages.flatMap { self =>
          val decls = self.declarations.distinct

          if (decls.exists(isBrokenSymbol)) None else {
//            println(s"\n \n Self:::: $self")
            decls.collect {
              // duplicated || blacklist cases
              case x if {
//                println(s"${x.fullName} ${x}")
                selfPackages.contains(x)
              } || unloads.contains(x.fullName) =>
                None -> None
              // other packages
              case x if x.isPackageDef =>
                Some(x) -> None
              // if static object || class def and mixed in AutoInject then recursive find dependencies
              case x
                // [info]    |                                       class : object : module class
                // [info]    |                    isRefinementClass  false : false : false
                // [info]    |                    isAbstractType     false : false : false
                // [info]    |                    isClassConstructor false : false : false
                // [info]    |                    isType             true : false : true
                // [info]    |                    isTerm             false : true : false
                // [info]    |                    isClassDef         true : false : true
                // [info]    |                    isTypeDef          false : false : false
                // [info]    |                    isValDef           false : true : false
                // [info]    |                    isDefDef           false : false : false
                // [info]    |                    isBind             false : false : false
                if maybeCandidates(x) =>
                None -> Some(x)
            }
          }
        } match {
          case x => x.flatMap(_._1) -> x.flatMap(_._2)
        }

        recursivePackageExplore(
          unloads,
          packages.distinctBy(_.fullName),
          joinSymbolSet(injectableSymbols, recursiveModuleExplore(modules))
        )

    }
  }

  private[this] def maybeCandidates(using q: Quotes)(x: q.reflect.Symbol, root: Boolean = true): Boolean = {
    var res: Boolean = false
    try {
      res = !isBrokenSymbol(x)
        && ((isClass(x) && q.reflect.TypeIdent(x).tpe.<:<(InjectableTag().tpe)) || isModule(x))
        && !x.flags.is(q.reflect.Flags.JavaDefined)
        && !x.flags.is(q.reflect.Flags.Trait)
        && !x.flags.is(q.reflect.Flags.Abstract)
        && (x != x.moduleClass || q.reflect.This(x).tpe == Types.NoType)
        && !x.isAbstractType
        && !x.isNoSymbol
    } catch {
      case _: Throwable =>
    }
    res
  }

  private def isBrokenSymbol(using q: Quotes)(symbol: q.reflect.Symbol): Boolean = {
    try {
      Class.forName(
        symbol.fullName
      )
      false
    } catch {
      case e: java.lang.NoClassDefFoundError =>
        true
      case e =>
        false
    }
  }

  private[this] def isClass(using q: Quotes)(symbol: q.reflect.Symbol): Boolean = symbol.isType && symbol.isClassDef && symbol != symbol.moduleClass

  private[this] def isModule(using q: Quotes)(symbol: q.reflect.Symbol): Boolean = symbol.isTerm && symbol.isValDef

  @tailrec
  private[this] final def recursiveModuleExplore(using q: Quotes)(
    n: Vector[q.reflect.Symbol],
    injectableSymbols: Iterable[q.reflect.TypeTree] = None
  ): Iterable[q.reflect.TypeTree] = {
    import q.reflect._
    n.distinct match {
      case accessibleSymbol if accessibleSymbol.isEmpty => injectableSymbols
      case accessibleSymbol =>
        val selection: Iterable[q.reflect.TypeTree] = accessibleSymbol.flatMap { x =>
          x.tree match {
            // (String, TypeTree, Option[Term])
            case ValDef(_, ttree, _) if ttree.tpe.baseType(InjectableTag().symbol).=:=(InjectableTag().tpe) =>
              Some(ttree)
            // (String, DefDef, List[Tree /* Term | TypeTree */], Option[ValDef], List[Statement])
            case ClassDef(_, _, _, _, _) if {
              TypeIdent(x).tpe.baseType(InjectableTag().symbol).=:=(InjectableTag().tpe)
            } =>
              Some(TypeIdent(x))
            case _ =>
              None
          }
        }
        recursiveModuleExplore(
          accessibleSymbol.withFilter(_.flags.is(Flags.Module)).flatMap { decl =>
            val children = decl.declarations
            if (children.exists(isBrokenSymbol)) None else children.filter(maybeCandidates(_, false))
          },
          joinSymbolSet(injectableSymbols, selection)
        )
    }
  }
}

