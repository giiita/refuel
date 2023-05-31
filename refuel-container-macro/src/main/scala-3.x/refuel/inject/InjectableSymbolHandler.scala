package refuel.inject

import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias

import scala.quoted._

object InjectableSymbolHandler extends LowLevelAPIConversionAlias {
  def filterTargetSymbols[T: Type](using q: Quotes)(symbols: Iterable[q.reflect.TypeTree]): Iterable[q.reflect.TypeTree] = {
    import q.reflect.*
    val tTypeRepr: TypeRepr = TypeT_TypeRepr[T]
    /**
      * class Foo[F[_]]
      * class FooImpl[F[_]] extends Foo[F] with AutoInject
      * inject[Foo[Future]]
      * 
      * = Foo[Future].baseType[FooImpl[F].baseType[Foo[Future]]]
      * = Foo[Future].baseType[Foo]
      * = Foo[Future]
      */
    symbols.filter { x =>
      // If the symbol is a subtype of the call type and has type parameters, it is required that all type parameters can be resolved with the type parameters of the call type
      x.tpe <:< tTypeRepr || {
        x.symbol.typeMembers.filter(!_.isNoSymbol).nonEmpty && x.symbol.typeMembers.filter(!_.isNoSymbol).forall(a => !tTypeRepr.typeSymbol.typeMember(a.name).isNoSymbol)
      }
    }.toList.sortBy[String](_.symbol.fullName)
  }

  /**
    * concreteTypeOf[Foo[Future]](FooImpl) == Foo[Future]
    * concreteTypeOf[Foo[Future]](BarImpl) == NoType
    *
    * @param q
    * @param symbol
    * @return
    */
  def concreteTypeOf[T: Type](using q: Quotes)(symbol: q.reflect.TypeTree): q.reflect.TypeRepr = {
    import q.reflect.*
    val callTypeRepr: TypeRepr = TypeT_TypeRepr[T]
    callTypeRepr.baseType(symbol.tpe.baseType(callTypeRepr.typeSymbol).typeSymbol)
  }

  /**
    * concreteKindTypesOf[Foo[Future]] == List[Future]
    * concreteKindTypesOf[Foo] == List[]
    *
    * @param q
    * @param symbol
    * @return
    */
  def concreteKindTypesOf(using q: Quotes)(callTypeRepr: q.reflect.TypeRepr): List[q.reflect.TypeRepr] = {
    import q.reflect.*
    callTypeRepr match {
      case AppliedType(_, args) => args
      case _ => Nil
    }
  }

  /**
    * concreteKindTypesOf[Foo[Future]] == List[Future]
    * concreteKindTypesOf[Foo] == List[]
    *
    * @param q
    * @param symbol
    * @return
    */
  def typeConcreteMap[T: Type](using q: Quotes): Map[String, q.reflect.TypeRepr] = {
    import q.reflect.*
    val callTypeRepr: TypeRepr = TypeT_TypeRepr[T]
    {
      callTypeRepr.typeSymbol.typeMembers.withFilter(!_.isNoSymbol).map(_.name) zip {
        callTypeRepr match {
          case AppliedType(_, args) => args
          case _ => Nil
        }
      }
    }.toMap
  }
}
