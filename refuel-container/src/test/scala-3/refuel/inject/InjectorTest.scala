package refuel.inject

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.container.provider.Lazy
import refuel.inject.InjectionPriority.Overwrite

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
import scala.util.Try
import javax.naming.Context

trait NonDependency
trait Dependency
object InjectionWithScopeDefinition {
 class DependencyImpl extends Dependency with AutoInject
}
object StandardInjectWithModule {
 trait ModuleBase

 object Module extends ModuleBase with AutoInject
}
object StandardInjectWithClass {
 trait ClassBase

 class Clazz extends ClassBase with AutoInject
}
object FailedInjectWithAbstractClass {
 trait Clazz                  extends ClassBase with AutoInject

 trait ClassBase

 abstract class AbstractClazz extends ClassBase with AutoInject
}
object FailedInjectWithSamePrioritySymbols {
 trait ClassBase {
   val value: Int
 }

 class Clazz1 extends ClassBase with AutoInject {
   override val value: Int = 1
 }

 class Clazz2 extends ClassBase with AutoInject {
   override val value: Int = 2
 }
}
object InjectWithDifferentPrioritySymbols {
 trait ClassBase {
   val value: Int
 }

 class Clazz1 extends ClassBase with AutoInject {
   override val value: Int = 1
 }

 @Inject[Overwrite]
 case object Clazz2 extends ClassBase with AutoInject {
   override val value: Int = 2
 }
}
object InjectWithDifferentUnaliasedPrioritySymbols {
 trait ClassBase {
   val value: Int
 }

 @Inject[InjectionPriority._3]
 class Clazz1 extends ClassBase with AutoInject {
   override val value: Int = 1
 }

 @Inject[Overwrite]
 class Clazz2 extends ClassBase with AutoInject {
   override val value: Int = 2
 }
}
object IterableTypesInjectUnionPriorities {
 trait IterableDependency
 class Default1 extends IterableDependency with AutoInject
 class Default2 extends IterableDependency with AutoInject

 @Inject[Overwrite]
 class Overwrite1 extends IterableDependency with AutoInject
 @Inject[Overwrite]
 class Overwrite2 extends IterableDependency with AutoInject
}
object Nested1 {
 trait NestedDependency

 class Nested2Class {
   trait NestedDependencyInterface    extends NestedDependency with AutoInject
   abstract class NestedDependencyAbs extends NestedDependency with AutoInject
   class NestedDependencyClass        extends NestedDependency with AutoInject
 }

 object Nested2 {
   object Nested3 {
     trait NestedDependencyInterface    extends NestedDependency with AutoInject
     abstract class NestedDependencyAbs extends NestedDependency with AutoInject
     class NestedDependencyClass        extends NestedDependency with AutoInject
   }
 }
}
object UninjectableConstructor {
 trait UninjectableConstructorDependency
 class WithString(value: String) extends UninjectableConstructorDependency with AutoInject
}
object InjectableConstructor {
 trait InjectableConstructorDependency

 trait InjectableParamDependency1

 trait InjectableParamDependency2

 class WithIndexedParam(val value: InjectableParamDependency1) extends InjectableConstructorDependency with AutoInject

 @refuel.inject.annotation.AutoInject
 case class InjectableParamDependency1Impl(p2: InjectableParamDependency2)
     extends InjectableParamDependency1
     with AutoInject

 object InjectableParamDependency2Impl extends InjectableParamDependency2 with AutoInject
}
object KindInjection {
 trait TP[T] {
   val t: T
 }
 class TpString extends TP[String] with AutoInject {
   val t: String = "string"
 }
 class TpInt extends TP[Int] with AutoInject {
   val t: Int = 10
 }
}
object KindInjectionByImplicitType {
  trait ContextType[T[_]] {
    val value: String
  }
  trait ContextType2[R[_]] {
    val value: Int
  }

  class CallSite[T1[_]: ContextType, T2[_]: ContextType2] extends AutoInject {
    def value(): String = s"${implicitly[ContextType[T1]].value}..${implicitly[ContextType2[T2]].value.toString()}"
  }
}
object ImplicitInjection {
 trait RequireImplicitDependency {
   val value: Dependency
   val ex: ImplicitlySymbol
 }

 class ImplicitlySymbol()

 class ImplicitCallsite(override val value: Dependency)(implicit override val ex: ImplicitlySymbol)
   extends RequireImplicitDependency
     with AutoInject
}
object UsingInjection {
 trait RequireImplicitDependency {
   val value: Dependency
   val ex: ImplicitlySymbol
 }

 class ImplicitlySymbol()

 class ImplicitCallsite(override val value: Dependency)(using override val ex: ImplicitlySymbol)
   extends RequireImplicitDependency
     with AutoInject
}
object FullPriorities {
 trait PriorityCheck
 @Inject[InjectionPriority._2]
 class __2 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._3]
 class __3 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._3]
 class ___3 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._4]
 class __4 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._1]
 class __1 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._5]
 class __5 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._5]
 class ___5 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._6]
 class __6 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._7]
 class __7 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._8]
 class __8 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._9]
 class __9 extends PriorityCheck with AutoInject
 @Inject[InjectionPriority._10]
 class __10 extends PriorityCheck with AutoInject
}

class InjectorTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
 "scopes" should {
   "Different instance in closed scope" in {
     import InjectionWithScopeDefinition.*
     val result: Dependency = inject[Dependency]
     assert(result.isInstanceOf[DependencyImpl])
     closed {
       val closedResult: Dependency = inject[Dependency]
       assert(result != closedResult)
     }
     val result2: Dependency = inject[Dependency]
     assert(result == result2)
   }
   "May reference same instance in extensible scope" in {
     import InjectionWithScopeDefinition.*
     val result: Dependency = inject[Dependency]
     assert(result.isInstanceOf[DependencyImpl])
     extended {
       val extendedResult: Dependency = inject[Dependency]
       assert(result == extendedResult)
     }
     val result2: Dependency = inject[Dependency]
     assert(result == result2)
   }
 }
 "inject" should {
   "Standard inject with module" in closed {
     import StandardInjectWithModule.*
     val result: ModuleBase = inject[ModuleBase]
     result shouldBe Module
   }
   "Standard inject with class" in closed {
     import StandardInjectWithClass.*
     val result: ClassBase = inject[ClassBase]
     val resultImpl: Clazz = inject[Clazz]
     assert(result.isInstanceOf[Clazz])
     assert(resultImpl.isInstanceOf[Clazz])
   }
   "Failed inject with abstract class" in closed {
     import FailedInjectWithAbstractClass.*
     assert(!compiletime.testing.typeChecks("val result: ClassBase = inject[ClassBase]"))
   }
   "Failed inject with same priority class" in closed {
     import FailedInjectWithSamePrioritySymbols.*
     assert(!compiletime.testing.typeChecks("val result: ClassBase = inject[ClassBase]"))
     val x1: ClassBase = inject[Clazz1]
     x1.value shouldBe 1
     val x2: ClassBase = inject[Clazz2]
     x2.value shouldBe 2
   }
   "Inject with different priorities" in closed {
     import InjectWithDifferentPrioritySymbols.*

     val result: ClassBase = inject[ClassBase]
     result.value shouldBe 2
   }
   "Inject with different unaliased priorities" in closed {
     import InjectWithDifferentUnaliasedPrioritySymbols.*

     val result: ClassBase = inject[ClassBase]
     result.value shouldBe 1
   }
   "Option type symbol injection" in closed {
     import InjectionWithScopeDefinition.*
     val result: Option[Dependency] = inject[Option[Dependency]]
     assert(result.nonEmpty)
     assert(result.get.isInstanceOf[DependencyImpl])

     val noFound: Option[NonDependency] = inject[Option[NonDependency]]
     assert(noFound.isEmpty)
   }
   "Option type symbol indexalize" in closed {
     import InjectionWithScopeDefinition.*
     val result1: Option[Dependency] = inject[Option[Dependency]]
     assert(result1.nonEmpty)
     val result2: Option[Dependency] = inject[Option[Dependency]]
     assert(result2.get.isInstanceOf[DependencyImpl])
     result1.get shouldBe result2.get
   }
   "Lazy Option type symbol injection" in closed {
     import InjectionWithScopeDefinition.*
     val result: Lazy[Option[Dependency]] = inject[Lazy[Option[Dependency]]]
     assert(result.nonEmpty)
     assert(result.get.isInstanceOf[DependencyImpl])

     val noFound: Option[NonDependency] = inject[Option[NonDependency]]
     assert(noFound.isEmpty)
   }
   "Iterable type symbol injection" in closed {
     import IterableTypesInjectUnionPriorities.*

     val result1: Iterable[IterableDependency] = inject[List[IterableDependency]]
     val result2: Iterable[IterableDependency] = inject[List[IterableDependency]]
     result1.size shouldBe 2
     assert((result1.toSet diff result2.toSet).isEmpty)
   }
   "Nested symbol injection" in closed {
     import Nested1.*

     val result: NestedDependency = inject[NestedDependency]
     assert(result.isInstanceOf[Nested2.Nested3.NestedDependencyClass])
   }
   "Failed unindexed constructor injection" in closed {
     import UninjectableConstructor.*
     assert(
       !compiletime.testing
         .typeChecks("val result: UninjectableConstructorDependency = inject[UninjectableConstructorDependency]")
     )
   }
   "Success undexed constructor injection" in closed {
     import InjectableConstructor.*
     val result: InjectableConstructorDependency = inject[InjectableConstructorDependency]
     assert(result.isInstanceOf[WithIndexedParam])
     assert(result.asInstanceOf[WithIndexedParam].value.isInstanceOf[InjectableParamDependency1Impl])
     assert(
       result
         .asInstanceOf[WithIndexedParam]
         .value
         .asInstanceOf[InjectableParamDependency1Impl]
         .p2
         .isInstanceOf[InjectableParamDependency2Impl.type]
     )
   }
    "Kind injection" in closed {
      import KindInjection.*
      val withString: TP[String] = inject[TP[String]]
      val withInt: TP[Int]       = inject[TP[Int]]
      assert(!compiletime.testing.typeChecks("val all: TP[_] = inject[TP[_]]"))
      val all: Iterable[TP[_]] = inject[List[TP[_]]]

      withString.t shouldBe "string"
      withInt.t shouldBe 10
      all.size shouldBe 2
      assert(all.map(_.t).forall(x => x == "string" || x == 10))
    }

    "kind injection resolved by implicit type" in closed {
      import KindInjectionByImplicitType.*
      
      implicit object ContextTypeForFuture extends ContextType[Future] {
        val value: String = "future"
      }
      implicit object ContextTypeForTry extends ContextType[Try] {
        val value: String = "try"
      }
      implicit object ContextTypeForLambda extends ContextType[({type Lambda[A] = Either[Throwable, A]})#Lambda] {
        val value: String = "lambda"
      }


      implicit object ContextType2ForFuture extends ContextType2[Future] {
        val value: Int = 1
      }
      implicit object ContextType2ForTry extends ContextType2[Try] {
        val value: Int = 2
      }
      implicit object ContextType2ForLambda extends ContextType2[({type Lambda[A] = Either[Throwable, A]})#Lambda] {
        val value: Int = 3
      }

      inject[CallSite[Try, Future]].value() shouldBe "try..1"
      inject[CallSite[Future, Future]].value() shouldBe "future..1"
      inject[CallSite[({type Lambda[A] = Either[Throwable, A]})#Lambda, Try]].value() shouldBe "lambda..2"
    }
 }

 "index" should {
   "Manual indexing" in closed {
     import InjectionWithScopeDefinition.*

     val result: Dependency = inject[Dependency]
     assert(result.isInstanceOf[DependencyImpl])

     class MyDependency extends Dependency
     val another = new MyDependency()
     another.index[Dependency]()

     val moreOnce: Dependency = inject[Dependency]
     assert(moreOnce.isInstanceOf[MyDependency])
   }
   "Propagate auto injection" in closed {
     import InjectionWithScopeDefinition.*

     var result: Seq[Dependency] = Seq.empty
     extended {
       result = result :+ inject[Dependency]
       assert(result.head.isInstanceOf[DependencyImpl])
     }
     result = result :+ inject[Dependency]
     assert(result.last.isInstanceOf[DependencyImpl])
     result.head shouldBe result.last
   }
   "No propagate manual injection" in closed {
     import InjectionWithScopeDefinition.*

     var result: Seq[Dependency] = Seq.empty
     extended {
       class MyDependency extends Dependency
       val another = new MyDependency()
       another.index[Dependency]()
       result = result :+ inject[Dependency]
       assert(result.head.isInstanceOf[MyDependency])
     }
     result = result :+ inject[Dependency]
     assert(result.last.isInstanceOf[DependencyImpl])
     result.head should not be result.last
   }
 }

 "acception scope" should {
   "allow by class" in closed {
     import InjectionWithScopeDefinition.*

     class AllowedScope extends Injector {
       val dependency: Dependency = inject[Dependency]
     }
     class DeniedScope extends Injector {
       val dependency: Dependency = inject[Dependency]
     }

     class MyDependency extends Dependency
     val another = new MyDependency()
     another
       .indexer[Dependency]()
       .accept(classOf[AllowedScope])
       .indexing()

     new AllowedScope().dependency shouldBe another
     assert(new DeniedScope().dependency.isInstanceOf[DependencyImpl])
   }
   "allow by instance" in closed {
     import InjectionWithScopeDefinition.*

     class AllowedScope extends Injector {
       lazy val dependency: Dependency = inject[Dependency]
     }
     class DeniedScope extends Injector {
       lazy val dependency: Dependency = inject[Dependency]
     }

     class MyDependency extends Dependency
     val another = new MyDependency()
     val allowed = new AllowedScope()
     another
       .indexer[Dependency]()
       .accept(allowed)
       .indexing()

     allowed.dependency shouldBe another
     assert(new AllowedScope().dependency.isInstanceOf[DependencyImpl])
     assert(new DeniedScope().dependency.isInstanceOf[DependencyImpl])
   }
   "implicit injection" in closed {
     import ImplicitInjection._
     import InjectionWithScopeDefinition._

     implicit val ex: ImplicitlySymbol = new ImplicitlySymbol()
     val result                        = inject[RequireImplicitDependency]
     assert(result.value.isInstanceOf[DependencyImpl])
     result.ex shouldBe ex
   }
   "using injection" in closed {
     import InjectionWithScopeDefinition._
     import UsingInjection._

     given ex: ImplicitlySymbol = new ImplicitlySymbol()
     val result                        = inject[RequireImplicitDependency]
     assert(result.value.isInstanceOf[DependencyImpl])
     result.ex shouldBe ex
   }
   "Priority inspection" in closed {
     import FullPriorities._

     val result: PriorityCheck = inject[PriorityCheck]
     assert(result.isInstanceOf[__1])
   }
 }
}