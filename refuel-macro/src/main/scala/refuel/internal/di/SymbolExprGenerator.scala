package refuel.internal.di

import refuel.container.anno.Effective
import refuel.exception.InjectDefinitionException

import scala.reflect.macros.blackbox

class SymbolExprGenerator[C <: blackbox.Context](c: C) {

  import c.universe._

  private[this] lazy val EffectiveConfigType = c.weakTypeOf[Effective]

  def generateExpr[T: c.WeakTypeTag](targets: Seq[C#Symbol]): C#Expr[T] = {
    targets.map { cnd =>
      cnd -> {
        cnd.annotations.find(_.tree.tpe.=:=(EffectiveConfigType))
          .flatMap(_.tree.children.tail.headOption)
          .map(_.symbol.asInstanceOf[C#Symbol])
      }
    }.partition(_._2.nonEmpty) match {
      case (_, non) if non.size > 1 =>
        // effected: ???, non effected > 1
        c.abort(c.enclosingPosition, s"Invalid dependency definition. There must be one automatic injection of inject[T] per priority. But found [${non.map(_._1).mkString(", ")}]")
      case (annotated, non) if annotated.isEmpty =>
        if (non.size == 1) {
          c.echo(c.enclosingPosition, s"${non.head._1.fullName} will be decided.")
          // effected: 0, non effected: 1
          pureGenerateExpr[T](non.head._1)
        } else {
          // effected: 0, non effected: 0
          c.abort(c.enclosingPosition, s"Can't find a dependency registration of ${c.weakTypeOf[T]}. Injection from runtime classpath must be given @RecognizedDynamicInjection.")
        }
      case (annotated, non) =>
        // effected > 0, non effected: 0 or 1

        c.echo(c.enclosingPosition, s"Effect is inspected in Runtime. These are inspected [${annotated.map(_._1.fullName).mkString(",")}]. Use ${non.headOption.map(_._1.fullName)} if everything is invalid.")

        // Effect and wrapped constructor list.
        val unlifted = annotated.map {
          case (a, b) =>
            reify[(() => T, Effect)] {
              (
                () => {
                  pureGenerateExpr[T](a).splice
                },
                pureGenerateExpr[Effect](b.get).splice
              )
            }
        }

        // Non effected wrapped constructor list
        val nonExpr = non.map(_._1).map { x =>
          reify[() => T] {
            () => {
              pureGenerateExpr[T](x).splice
            }
          }
        }

        reify {
          val _nonAnnotatedCands: Seq[() => T] = c.Expr[Seq[() => T]](q"Seq(..$nonExpr)").splice
          c.Expr[Seq[(() => T, Effect)]](q"Seq(..$unlifted)").splice.filter(_._2.activate) match {
            // If annotated found.
            case x if x.size == 1 => x.head._1()
            case x if x.isEmpty =>
              // If annotated not found and non annotated
              if (_nonAnnotatedCands.isEmpty) {
                throw new InjectDefinitionException(s"Can't find a dependency registration. Injection from runtime classpath must be given @RecognizedDynamicInjection.")
              } else {
                _nonAnnotatedCands.head()
              }
            case x =>
              throw new InjectDefinitionException(s"Multiple valid effects were found of ${x.map(_._2).mkString(",")}.")
          }
        }
    }
  }

  def pureGenerateExpr[T: c.WeakTypeTag](target: C#Symbol): C#Expr[T] = {
    if (target.isModule) {
      c.Expr[T](c.parse(target.fullName))
    } else {
      c.Expr[T] {
//        Vector[(String, String)]().grou
        c.parse(
          s"""new ${target.fullName}(${
            val a = target.asClass.primaryConstructor.asMethod
            val b = a.paramLists
            target.asClass.primaryConstructor.asMethod.paramLists.collect {
              case curry if !curry.exists(_.isImplicit) =>
                curry.map { param =>
                  s"inject[${param.typeSignature.toString}]"
                }.mkString(",")
            }.mkString(")(")
          }) with refuel.injector.Injector"""
        )
      }
    }
  }
}
