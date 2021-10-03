package refuel.json.`macro`

import refuel.json.JsonVal
import refuel.json.codecs.Codec
import refuel.json.exception.InvalidDeserializationException

import scala.reflect.macros.blackbox
import scala.util.{Failure, Success}

object TypedCodecMacro {

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
  def fromInferOrCase[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Codec[T]] = {
    import c.universe._
    c.inferImplicitValue(weakTypeOf[Codec[T]]) match {
      case x if x.isEmpty => generateCodec[T](c)
      case x => c.Expr[Codec[T]](x)
    }
  }

  protected def recallWithTag[T](c: blackbox.Context)(q: c.WeakTypeTag[T]): c.Expr[Codec[T]] = {
    import c.universe._
    c.Expr(q"Derive[$q]")
  }

  protected def recallWithType[T](c: blackbox.Context)(q: c.Type): c.Expr[Codec[T]] = {
    import c.universe._
    c.Expr(q"Derive[$q]")
  }

  protected def recallWithSymbol[T](c: blackbox.Context)(q: c.Symbol): c.Expr[Codec[T]] = {
    import c.universe._
    c.Expr(q"Derive[${q.fullName}]")
  }

  private[this] def generateCodec[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Codec[T]] = {
    import c.universe._
    val implicitExistence = inferImplicitCodecFactory(c)(c.weakTypeOf[T])
    if (implicitExistence._2.nonEmpty) {
      c.Expr[Codec[T]](q"""${implicitExistence._2}(..${implicitExistence._1.map(recallWithType(c)(_))})""")
    } else createNewCodecTree[T](c)()
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
  private[this] def inferRequireFunctionFactory(c: blackbox.Context)(
                                                 tpe: c.Type,
                                                 chType: List[c.Type]
                                               ): c.Tree = {
    import c.universe._
    val children = chType.map(_x => appliedType(weakTypeOf[Codec[_]], _x))

    val childrenTup = if (children.size > 1) {
      appliedType(
        weakTypeOf[AnyVal].typeSymbol.owner.info
          .member(TypeName(s"Tuple${children.size}")),
        children
      )
    } else children.headOption.getOrElse(NoType)

    val to = appliedType(weakTypeOf[Codec[_]], tpe)

    // Ex. Find this.(Codec[String] => Codec[Seq[String]]) implicit view.
    c.inferImplicitView(q"this", childrenTup, to)
  }

  /**
    * Find a implicit def view.
    *
    * @param tpe Construct single type. Ex: Seq[String], Option[String], Map[Int, String]
    * @return Children type and Codec[T] => Codec[C[T] ] infer view.
    */
  private[this] def inferImplicitCodecFactory(c: blackbox.Context)(
                                               tpe: c.Type
                                             ): (List[c.Type], c.Tree) = {
    import c.universe._
    tpe.dealias match {
      case PolyType(x, _) =>
        x.map(_.typeSignature) -> inferRequireFunctionFactory(c)(
          tpe,
          x.map(_.typeSignature)
        )
      case _: ClassInfoType =>
        List() -> inferRequireFunctionFactory(c)(tpe, List())
      case TypeRef(_, _, z) =>
        z -> inferRequireFunctionFactory(c)(tpe, z)
    }
  }

  private[this] def createChildDeserializationTrees(c: blackbox.Context)(
                                                     ap: c.universe.MethodSymbol,
                                                     paramNames: List[c.universe.Symbol#NameType]
                                                   ): List[c.universe.Tree] = {
    import c.universe._

    if (ap.returnType.<:<(weakTypeOf[AnyVal])) {
      paramNames.zipWithIndex.map { x => q"""${c.parse(s"self._codecChildren._${x._2 + 1}")}.deserialize(${c.parse("bf")})""" }
    } else {
      paramNames.zipWithIndex.map { x =>
        q"""
          {
            ${c.parse(s"""val name: String = "${x._1.decodedName.toTermName}" """)}
            ${c.parse(s"self._codecChildren._${x._2 + 1}")}.deserialize(${c.parse("bf")}.named(name))
          }
         """
      }
    }
  }

  /**
    * If there is no implicit Codec, extract apply / unapply and generate Codec.
    *
    * @tparam T
    * @return
    */
  private[this] def createNewCodecTree[T: c.WeakTypeTag](c: blackbox.Context)(): c.Expr[Codec[T]] = {
    import c.universe._
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
        case Some(s) =>
          scala.util.Try(s.asMethod).getOrElse {
            c.abort(
              c.enclosingPosition,
              s"Overloaded constructors cannot automatically generate Codecs. ${weakTypeOf[T].typeSymbol}"
            )
          }
      }
    }

    fromApply[T](c)(ap, up)
  }

  private[this] def fromApply[T: c.WeakTypeTag](c: blackbox.Context)(
                                               ap: c.universe.MethodSymbol,
                                               up: c.universe.MethodSymbol
                                             ): c.Expr[Codec[T]] = {
    import c.universe._
    val paramNames: List[Symbol#NameType] = ap.paramLists.headOption.toList.flatMap(_.map(_.name))

    val childCodecs: Seq[c.Expr[Codec[_]]] = up.returnType match {
      // This is codec for constructor that has result type be Tuple.
      case TypeRef(_, _, arg :: _) if arg.typeSymbol.name.toString.startsWith("Tuple") =>
        (ap.paramLists.headOption.toList.flatten, arg) match {
          // For tuple codecs.
          // Example, case class Sample(value: (String, Int))
          // Sample unapply function return tuple that equal to [[ case class Sample(value: String, value: Int) ]]
          case (x, TypeRef(_, _, _))
            if x.headOption.fold(false)(
              _.typeSignature.typeSymbol.name.toString.startsWith("Tuple")
            ) =>
            Seq(recallWithSymbol(c)(x.head))
          // For other case class codecs.
          case (_, TypeRef(_, _, args)) => args.map(recallWithType(c)(_))
        }
      // This is codec for constructor that has result type not be Tuple.
      // Example, Seq[T].unapplySeq, Vector[T].unapplySeq etc...
      case TypeRef(_, _, arg :: _) => Seq(recallWithType(c)(arg))
      // case _ => c.abort(c.enclosingPosition, s"Unsupported unapply return type signature. ${up.returnType}")
    }

    val serializeTrees: c.Tree = up.returnType match {
      // This is codec for constructor that has result type be Tuple.
      case TypeRef(_, _, arg :: _) if arg.typeSymbol.name.toString.startsWith("Tuple") =>
        (ap.paramLists.headOption.toList.flatten, arg) match {
          // For tuple codecs.
          // Example, case class Sample(value: (String, Int))
          // Sample unapply function return tuple that equal to [[ case class Sample(value: String, value: Int) ]]
          case (x, TypeRef(_, _, constructs))
            if x.headOption.fold(false)(
              _.typeSignature.typeSymbol.name.toString.startsWith("Tuple")
            ) =>
            val typeInTuple: Seq[Tree] = (1 to constructs.size).map { i =>
              q"""
                 implicit val ${c.parse(s"_codecInsertion$i")} = ${c.parse(s"self._codecChildren")}
               """
            }
            q"""
               ..$typeInTuple
               refuel.json.Json.obj(
                 ${c.parse(s""""${paramNames.head.toTermName}"""")} -> ${recallWithSymbol(c)(x.head)}.serialize(unapplied)
               )
             """
          // For other case class codecs.
          case (_, TypeRef(_, _, args)) =>
            val result = paramNames.zip(args.zipWithIndex).map {
              case (name, (argType, index)) =>
                c.Expr[(String, JsonVal)](q"""
                ${c.parse(s""""${name.toTermName}"""")} -> ${c
                  .parse(s"self._codecChildren._${index + 1}")}.serialize(${c.parse(s"unapplied._${index + 1}")})
                  """)
            }
            q"""
               refuel.json.Json.obj(
                 ..$result
               )
             """
        }
      case TypeRef(_, _, arg :: _) if ap.returnType.<:<(weakTypeOf[AnyVal]) =>
        q"""${c.parse(s"self._codecChildren._1")}.serialize(unapplied)"""
      // This is codec for constructor that has result type not be Tuple.
      // Example, Seq[T].unapplySeq, Vector[T].unapplySeq etc...
      case TypeRef(_, _, arg :: _) =>
        q"""
           refuel.json.Json.obj(
             ${c.parse(s""""${paramNames.head.toTermName}"""")} -> ${c.parse(s"self._codecChildren._1")}.serialize(unapplied)
           )
         """
    }

    reify {
      new Codec[T] {
        self =>

        private[this] val _codecChildren = c.Expr(
          if (childCodecs.size == 1) q"""scala.Tuple1(${childCodecs.head})""" else q"""(..$childCodecs)"""
        ).splice

        def fail(bf: JsonVal, e: Throwable): Throwable = {
          InvalidDeserializationException(
            s"Cannot deserialize $bf into ${c.Expr(c.reifyRuntimeClass(weakTypeOf[T])).splice}",
            e
          )
        }

        override def serialize(t: T): JsonVal = {
          c.Expr[JsonVal](q"""
                 val unapplied = ${weakTypeOf[T].typeSymbol.companion}.$up(${c.parse("t")}).get
                 $serializeTrees
                """).splice
        }

        override def deserialize(bf: JsonVal): T = {
          scala.util.Try {
            c.Expr[T](
              q"${weakTypeOf[T].typeSymbol.companion}.$ap(..${createChildDeserializationTrees(c)(ap, paramNames)})"
            )
              .splice
          } match {
            case Success(r) => r
            case Failure(e) => throw fail(bf, e)
          }
        }
      }
    }
  }
}
