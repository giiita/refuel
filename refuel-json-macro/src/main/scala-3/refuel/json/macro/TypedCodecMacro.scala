package refuel.json.`macro`

import refuel.container.macros.internal.DependencyRankings.{ValDefModule_Expr, constructInjectionTerm, implicitInjectionTerm, isDependencyPoolRef}
import refuel.inject.InjectableTag
import refuel.inject.Types.LocalizedContainer
import refuel.json.codecs.{Codec, CodecTypeProjection, Write}
import refuel.json.compiletime.DeriveWrites.inferWrite
import refuel.json.{ImplicitCodecDefSupport, Json, JsonVal}

import scala.annotation.tailrec
import scala.quoted.{Expr, Quotes, Type}
import scala.util.Try

object TypedCodecMacro {
  def tupleDerivings[T <: Tuple: Type](using q: Quotes): Expr[Tuple.Map[T, Codec]] = {
    import q.reflect.*

    val tupType = TypeRepr.of[T]

    val derivedCodecs = tupType match {
      case AppliedType(_, ttypes) =>
        ttypes.map { tpe =>
          Select.unique(
            This(Symbol.requiredClass("refuel.json.JsonTransform")),
            "Derive"
          ).appliedToType(tpe).asExpr
        }
    }

    Expr.ofTupleFromSeq(derivedCodecs).asInstanceOf[Expr[Tuple.Map[T, Codec]]]
  }

  /** Derive a Codec of the specified type from the current scope.
    *
    * This is solved in the following order.
    * 1. implicit `Codec[T]` instance
    * 2. implicit conversion of `Codec[T] => Codec[K[T]]`.
    * 3. implicit conversion of `Tuple.Map[T <: Tuple, Codec] => Codec[T]`
    * 4. signature analysis of `Product` subtypes
    *
    * 1. implicit Codec[T] instance
    *    If there is already an implicit Codec[T], and Derive binds it to another
    *    implicit property, an infinite loop may occur and it may not compile.
    *
    *    opaque type codec derivation must be in the scope of opaque type.
    *
    * @param q Quotes
    * @tparam T AnyType. If no implicit codec is defined, the types upper as Product.
    *           Compilation fails if no derivation is possible.
    * @return
    */
  def derivings[T: Type](using q: Quotes): Expr[Codec[T]] = {
    import q.reflect._
    val TTypeRepr = TypeRepr.of[T]

    TTypeRepr match {
      // Search opaque super type codec
      case x@TypeRef(a, b) if x.isOpaqueAlias =>
        Implicits.search(TypeRepr.of[[X] =>> Codec[X]].appliedTo(x.translucentSuperType)) match {
          case iss: ImplicitSearchSuccess if Symbol.spliceOwner.owner.isLocalDummy || !isInfiniteLoopPossibility(iss.tree.symbol.pos, Symbol.spliceOwner.owner.pos) =>
            iss.tree.asExprOf[Codec[T]]
          case _ => report.throwError(s"No found super type codec of opaque type for ${TTypeRepr.show}")
        }
      case x =>
        // 1. Search for implicit codecs in scope that have already been defined
        Implicits.search(TypeRepr.of[Codec[T]]) match {
          case iss: ImplicitSearchSuccess if Symbol.spliceOwner.owner.isLocalDummy || !isInfiniteLoopPossibility(iss.tree.symbol.pos, Symbol.spliceOwner.owner.pos) =>
            iss.tree.asExprOf[Codec[T]]
          case x =>
            // If implicit Codec is not found, search for a function that can refer to Codec by implicit conversion
            Implicits.search(TypeRepr.of[scala.Conversion[_, Codec[T]]]) match {
              // If there is an implicit conversion, derive the source value and pass it to the implicit conversion function
              case iss: ImplicitSearchSuccess =>
                iss.tree.tpe.asType match {
                  case '[scala.Conversion[Codec[t], Codec[T]]] =>
                    '{
                      ${iss.tree.asExprOf[scala.Conversion[Codec[t], Codec[T]]]}.apply(${TypedCodecMacro.derivings[t]})
                    }
                  case '[scala.Conversion[z, Codec[T]]] =>
                    val xx = Select.unique(
                      This(Symbol.requiredClass("refuel.json.JsonTransform")),
                      "TupleCodecDerivation"
                    ).appliedToType(TTypeRepr).asExprOf[z]
                    '{
                      ${iss.tree.asExprOf[scala.Conversion[z, Codec[T]]]}.apply(${xx})
                    }
                  case x =>
                    report.throwError(s"Cannot derivins codec of type: ${iss.tree.show}")
                }
              // Even without implicit codec or implicit conversion, if the target is a Product, it will be derived
              case _: ImplicitSearchFailure if TTypeRepr.<:<(TypeRepr.of[Product]) =>
                val maybeAppl = TTypeRepr.typeSymbol.companionModule.declaredMethod("apply").headOption

                val maybeAutoCodec = for {
                  appl <- maybeAppl
                } yield {
                  val nameAndCodecss = appl.tree match {
                    case DefDef(_, paramss, _, _) => paramss.map { block =>
                      block.params.collect {
                        case ValDef(name, typ, term) =>
                          typ.tpe.asType match {
                            case '[t] => name -> TypedCodecMacro.derivings[t]
                          }
                      }
                    }
                  }
                  generateCodecExprFor[T](nameAndCodecss)
                }

                maybeAutoCodec.getOrElse {
                  report.throwError(s"No found apply method of ${TypeTree.of[T].symbol.fullName}")
                }
              case _ => report.throwError(s"No found codec of type ${TTypeRepr.typeSymbol.fullName}")
            }
        }
    }
  }

  private final def isInfiniteLoopPossibility(using q: Quotes)(pos1: Option[q.reflect.Position], pos2: Option[q.reflect.Position]): Boolean = {
    pos1 == pos2 && Try(pos1.map(_.start)).toOption.flatten == Try(pos2.map(_.start)).toOption.flatten
  }

  /** Implement Codec from constructor parameters
    *
    * @param q Quotes
    * @param nameAndCodecss The name of the constructor and its child Codec
    * @tparam T
    * @return
    */
  private final def generateCodecExprFor[T: Type](using q: Quotes)(nameAndCodecss: List[List[(String, Expr[Codec[?]])]]): Expr[Codec[T]] = {
    import q.reflect.*
    val TTypeRepr = TypeRepr.of[T]
    '{
    new Codec[T] {
      override def deserialize(bf: JsonVal): T = ${
      Select.unique(
        Ref(TTypeRepr.typeSymbol.companionModule),
        "apply"
      ).appliedToArgss(
        nameAndCodecss.map {
          _.map {
            case (name, codecExpr) =>
              val ex = '{ CodecTypeProjection.given_CodecTypeProjection_Codec.read(bf.named(${Expr(name)}))(using ${codecExpr}.asInstanceOf) }
              ex.asTerm
          }
        }
      ).asExprOf[T]
      }
      override def serialize(t: T): JsonVal = refuel.json.Json.obj(
        ${
        Expr.ofSeq(
          nameAndCodecss.flatten.toList.zipWithIndex.map {
            case ((name, codecExpr), elmIndex) => '{ ${Expr(name)} -> ${codecExpr}.asInstanceOf[Codec[Any]].serialize(t.asInstanceOf[Product].productElement(${Expr(elmIndex)})) }
          }
        )
        }*
      )
    }
    }
  }
}
