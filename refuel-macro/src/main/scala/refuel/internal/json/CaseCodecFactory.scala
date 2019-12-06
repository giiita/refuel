package refuel.internal.json

import refuel.internal.PropertyDebugModeEnabler
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializeType}
import refuel.json.{Codec, Json}

import scala.reflect.macros.blackbox
import scala.util.{Failure, Success}

class CaseCodecFactory(val c: blackbox.Context)
  extends PropertyDebugModeEnabler {

  import c.universe._

  protected def recall[T](q: WeakTypeTag[T]): c.Expr[Codec[T]] =
    c.Expr(q"refuel.json.codecs.factory.InferImplicitCodec.from[$q]")

  protected def recall[T](q: Type): c.Expr[Codec[T]] =
    c.Expr(q"refuel.json.codecs.factory.InferImplicitCodec.from[$q]")

  protected def recall[T](q: Symbol): c.Expr[Codec[T]] =
    c.Expr(q"refuel.json.codecs.factory.InferImplicitCodec.from[$q]")

  /**
    * Create Codec[T] instance.
    * T must have apply and unapply or unapplySeq implemented,
    * and the argument type of apply and the return parameter of unapply must match.
    * In other words, T is a case class.
    *
    * If the above conditions are satisfied,
    * there is a possibility that all codecs that are required internally will be built.v
    *
    * If there is an implicit internal Codec,
    * it will be used, otherwise it will be automatically generated from apply / unapply
    *
    *
    * - [ Note ]
    * If recursive resolution is not possible and there are not enough user definitions to make up for it,
    * a compilation error will occur.
    *
    *
    * For example, there is a trait field declaration inside,
    * a class that does not have apply / unapply, or a type class that is not supported by default.
    * In that case, you need to declare a custom Codec by the user.
    *
    * @tparam T Codec build type
    * @return
    */
  def fromCaseClass[T: c.WeakTypeTag]: Expr[Codec[T]] = {
    debuglog(c)(s"Get for ${weakTypeOf[T]}")
    generateCodec[T]
  }

  def fromInferOrCase[T: c.WeakTypeTag]: Expr[Codec[T]] = {
    debuglog(c)(s"Get for ${weakTypeOf[T]}")
    c.inferImplicitValue(weakTypeOf[Codec[T]]) match {
      case x if x.isEmpty => generateCodec[T]
      case x => c.Expr[Codec[T]](x)
    }
  }

  private[this] def generateCodec[T: c.WeakTypeTag]: c.Expr[Codec[T]] = {
    val implicitExistence = inferImplicitCodecFactory(weakTypeOf[T])
    if (implicitExistence._2.nonEmpty) {
      c.Expr[Codec[T]](
        q"""
            ${implicitExistence._2}(..${implicitExistence._1.map(recall)})""")
    } else createNewImplicitTree[T]()
  }

  /**
    * Search for an implicit view to Codec[T].
    * When T needs a type parameter and an implicit type parameter type codec,
    * you can get a fully considered view by unpacking the Type reference.
    *
    * {{{
    *   inferRequireFunctionFactory(Map[String, Int, Option[Long]], List(String, Int, Option[Long])
    *     = (implicit a: Codec[String], b: Codec[Int], c: Codec[Option[Long]]) => Codec[Map[String, Int, Option[Long]]]
    * }}}
    *
    * @param tpe    Target type symbol instance.
    * @param chType Implicitly required Codec children type.
    * @return
    */
  private[this] def inferRequireFunctionFactory(
                                                 tpe: c.Type,
                                                 chType: List[c.Type]
                                               ): c.Tree = {
    val children = chType.map(_x => appliedType(weakTypeOf[Codec[_]], _x))

    val childrenTup = if (children.size > 1) {
      appliedType(
        weakTypeOf[AnyVal].typeSymbol.owner.info
          .member(TypeName(s"Tuple${children.size}")),
        children
      )
    } else children.headOption.getOrElse(NoType)

    val to = appliedType(weakTypeOf[Codec[_]], tpe)

    val implicitExistence = c.inferImplicitView(q"this", childrenTup, to)

    debuglog(c)(
      s"""
         |implicit found for $childrenTup => $to = $implicitExistence
         |""".stripMargin)
    implicitExistence
  }

  /**
    * Find a implicit def view.
    *
    * @param tpe Construct single type. Ex: Seq[String], Option[String], Map[Int, String]
    * @return Children type and Codec[T] => Codec[C[T] ] infer view.
    */
  private[this] def inferImplicitCodecFactory(
                                               tpe: c.Type
                                             ): (List[c.Type], c.Tree) = {
    tpe match {
      case PolyType(x, _) =>
        x.map(_.typeSignature) -> inferRequireFunctionFactory(
          tpe,
          x.map(_.typeSignature)
        )
      case _: ClassInfoType =>
        List() -> inferRequireFunctionFactory(tpe, List())
      case TypeRef(_, _, z) =>
        z -> inferRequireFunctionFactory(tpe, z)
    }
  }

  private[this] def createChildDeserializationTrees(
                                                     ap: MethodSymbol,
                                                     paramNames: List[Symbol#NameType]
                                                   ): List[c.universe.Tree] = {
    val typers: List[c.Expr[Codec[_]]] =
      ap.paramLists.headOption.toList.flatten.map {
        case x@TypeRef(_, _, z)
          if z.nonEmpty && inferImplicitCodecFactory(x.typeSignature)._2.nonEmpty =>
          val enpr = inferImplicitCodecFactory(x.typeSignature)

          c.Expr[Codec[_]](
            q"""
                 ${enpr._2}(..${enpr._1.map(recall)})""")
        case x =>
          recall(x)
      }

    typers.zip(paramNames).map { x =>
      q"""
          ${
        c
          .parse(s"""val name: String = "${x._2.decodedName.toTermName}" """)
      }
          ${x._1.tree}.deserialize(implicitly[${weakTypeTag[Json]}].named(name)) match {
                case Right(b) => b
                case Left(e)  => throw e
              }"""
    }
  }

  /**
    * If there is no implicit Codec, extract apply / unapply and generate Codec.
    *
    * @tparam T
    * @return
    */
  private[this] def createNewImplicitTree[T: WeakTypeTag]()
  : c.Expr[Codec[T]] = {

    val (ap, up) = {
      weakTypeOf[T].companion.decl(TermName("apply")) match {
        case NoSymbol =>
          c.abort(
            c.enclosingPosition,
            s"No apply function found for ${weakTypeOf[T]}"
          )
        case x => x.asMethod
      }
    } -> {
      Seq(
        weakTypeOf[T].companion.decl(TermName("unapply")),
        weakTypeOf[T].companion.decl(TermName("unapplySeq"))
      ).find(_ != NoSymbol) match {
        case None =>
          c.abort(
            c.enclosingPosition,
            s"No unapply or unapplySeq function found for ${weakTypeOf[T]}"
          )
        case Some(s) => s.asMethod
      }
    }

    fromApply[T](ap, up)
  }

  private[this] def fromApply[T: WeakTypeTag](
                                               ap: MethodSymbol,
                                               up: MethodSymbol
                                             ): c.Expr[Codec[T]] = {
    val paramNames = ap.paramLists.headOption.toList.flatMap(_.map(_.name))

    val serializeTrees = up.returnType match {
      // This is codec for constructor that has result type be Tuple.
      case TypeRef(_, _, arg :: _)
        if arg.typeSymbol.name.toString.startsWith("Tuple") =>
        (ap.paramLists.headOption.toList.flatten, arg) match {
          // For tuple codecs.
          // Example, case class Sample(value: (String, Int))
          // Sample unapply function return tuple that equal to [[ case class Sample(value: String, value: Int) ]]
          case (x, TypeRef(_, _, constructs))
            if x.headOption.fold(false)(
              _.typeSignature.typeSymbol.name.toString.startsWith("Tuple")
            ) =>
            val typeInTuple = constructs.zipWithIndex.map { const =>
              q"""
                 implicit val ${c.parse(s"_${const._2}")} = ${recall(const._1)}
               """
            }
            Seq(c.Expr[(String, Json)](
              q"""
                  ..$typeInTuple
                  ${
                c
                  .parse(s""""${paramNames.head.toTermName}"""")
              } -> ${
                recall(
                  x.head
                )
              }.serialize(${c.parse(s"unapplied")})
                """))
          // For other case class codecs.
          case (_, TypeRef(_, _, args)) =>
            paramNames.zip(args.zipWithIndex).map {
              case (name, (argType, index)) =>
                c.Expr[(String, Json)](
                  q"""
                ${c.parse(s""""${name.toTermName}"""")} -> ${recall(argType)}.serialize(${
                    c
                      .parse(s"unapplied._${index + 1}")
                  })
                  """)
            }
        }
      // This is codec for constructor that has result type not be Tuple.
      // Example, Seq[T].unapplySeq, Vector[T].unapplySeq etc...
      case TypeRef(_, _, arg :: _) =>
        Seq(c.Expr[(String, Json)](
          q"""
                ${c.parse(s""""${paramNames.head.toTermName}"""")} -> ${
            recall(
              arg
            )
          }.serialize(${c.parse(s"unapplied")})
              """))
      // case _ => c.abort(c.enclosingPosition, s"Unsupported unapply return type signature. ${up.returnType}")
    }

    reify {
      new Codec[T] {
        def fail(bf: Json, e: Throwable): DeserializeFailed = {
          UnexpectedDeserializeType(
            s"Cannot deserialize to ${c.Expr(c.reifyRuntimeClass(weakTypeOf[T])).splice} -> ${bf.toString}",
            e
          )
        }

        override def serialize(t: T): Json = {
          implicit def v: T = t

          c.Expr[Json](
            q"""
             val unapplied = ${weakTypeOf[T].typeSymbol.companion}.$up(implicitly[${
              weakTypeOf[
                T
                ]
            }]).get
             refuel.json.entry.JsObject.apply(
               Seq(..$serializeTrees): _*
             )
            """)
            .splice
        }

        override def deserialize(bf: Json): Either[DeserializeFailed, T] = {
          implicit def json: Json = bf

          scala.util.Try {
            c.Expr[T](
              q"${weakTypeOf[T].typeSymbol.companion}.$ap(..${createChildDeserializationTrees(ap, paramNames)})"
            )
              .splice
          } match {
            case Success(r) => Right(r)
            case Failure(e) => Left(fail(bf, e))
          }
        }
      }
    }
  }
}
