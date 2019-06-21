# scaladia-container

## Usage

```
libraryDependencies += "com.phylage" %% "scaladia-container" % "2.0.0"
````

### The simplest Injection

```scala
object A extends A with AutoInject[A]

trait A {
  def toString: String = "TEST"
}
```

```scala
object TestA extends Injector {
  println(inject[A].toString) // "TEST"
}
```

### Constructor dependency injection 


```scala
object A extends A with AutoInject[A]

trait A {
  def exec: String = "TEST"
}
```

```scala
object TestA extends Injector {
  // Need not be lazy val
  // It is implicitly initialized when accessed for the first time
  // However, specifying a type is deprecated because it is initialized in the constructor.
  // ex:) `private val a: A = inject[A]` 
  private[this] val a = inject[A] // a's type is Lazy[A]

  println(a.exec) // TEST
}
```

Classes that inherit AutoInjector are automatically registered at macro expansion (when compiled).<br/>
Dependencies defined as inner objects of trait / class other than object can not be registered automatically.<br/>
<br/>

In AutoInjector, if multiple dependencies of the same type are registered, scaladia will automatically insert the highest priority first deployed module.
If you want to control multiple overlapping dependencies, there are several approaches.<br/>
<br/>
<br/>

### case 1. Custom priority injection
```scala
/**
 * AutoInject priority = 1000
 * Injector.flush[X](x) = 1100
 * AutoInjectCustomPriority = ???
 * RecoveredInject priority = 0
 * narrow[X](X).indexing() priority = Int.MAX
 */
object A extends AutoInject[A] with A
object B extends AutoInjectCustomPriority[A](1001) with A {
  override def toString: String = "I am B."
}

trait A {
  def toString: String = "I am A"
}
```


```scala
object TestA extends Injector {
  println(inject[A].toString) // "I am B."
}
```

### case 2. Tagging injection
```scala
trait AliasA
trait AliasB
trait AliasC

object A extends A with Tag[AliasA] with AutoInject[A @@ AliasA]
object B extends A with Tag[AliasB] with AutoInject[A @@ AliasB] {
  override def toString: String = "I am B."
}
object C extends A with Tag[AliasC] with AutoInject[A] { // I have not AutoInject tag
  override def toString: String = "I am C."
}

trait A {
  def toString: String = "I am A"
}
```


```scala
object TestA extends Injector {
  println(inject[A].toString) // "I am C.". Because scaladia recognizes objects that inherit tags as different objects
  
  println(inject[A @@ AliasA].toString) // "I am A."
  
  println(inject[A @@ AliasB].toString) // "I am B."
}
```



### Testing

When UnitTest parallel execution is enabled, overriding global scope dependencies such as `depends` in a test may result in unexpected overwrites between different threads.<br/>
Therefore, be careful to use narrowly overriding in unit tests.

```scala
class XxxTest extends TestClient with Injector {
  "Test" should "test-1" in {
    val targetService = TargetServiceImpl
    narrow[A](new MockA).accept(targetService).indexing()
    targetService.exec() // MockA is used for A in targetService
  }
  
  trait Context extends TargetService
  "Test" should "another case" in new Context {
    narrow[A](new MockA).accept(this).indexing()
    exec()
  }
}
```

### Override dependency

```scala
object A extends AutoInject[A]

trait A {
  def toString: String = "TEST"
}
```

```scala
object TestA extends Injector {
  private[this] val a: A = inject[A]

  flush[A] {
    new A {
      override def toString: String = "OVERRIDE"
    }
  }

  def test = {
    println(a.toString) // OVERRIDE
  }
}
```

When overwriting with a limited scope in the test, We recommend it.<br>
```scala
narrow[T](new T {}).accept(this).indexing()
```
Overwriting with `depends` and running tests in parallel may unexpectedly overwrite globally.


### Custom usage

```scala
  trait A
  object B extends A
  object C extends A
  
  trait BInjector extends Injector {
    flush[A](B)
  }
  
  object Test extends BInjector {
    def test = {
      println(inject[A]) // B
    }
  }
  
```

### This will not work

```scala
  trait A
  object B extends A
  object C extends A
  
  trait X
  object Y extends X
  object Z extends X
  
  trait BInjector extends Injector {
    flush[A](B)
  }
  
  trait ZInjector extends Injector {
    flush[X](Z)
  }
  
  object TestA extends BInjector {
    def test = {
      println(inject[A]) // B
    }
  }
  
  object TestX extends ZInjector {
    def test = {
      println(inject[A]) // Uninjectable object. trait A
    }
  }
```

### It corresponds to linearization.

```scala
  trait A
  object B extends A
  object C extends A
  
  trait BInjector extends Injector {
    flush[A](B)
  }
  
  trait CInjector extends Injector {
    flush[A](C)
  }
  
  object TestA extends BInjector with CInjector {
    def test = {
      println(inject[A]) // C
    }
  }
```

### It also supports type erase.

```scala
  trait A[T]
  object B extends A[List[String]] with AutoInject[A[List[String]]]
  object C extends A[List[BigDecimal]] with AutoInject[A[List[BigDecimal]]]

  object TestA extends Injector {
    def test = {
      println(inject[A[List[String]]]) // B
      println(inject[A[List[BigDecimal]]]) // C
    }
  }
```

However, caution is required as types inheriting AnyVal are handled internally as java.lang.Object.

```scala
  trait A[T]
  object B extends A[List[Int]] with AutoInject[A[List[Int]]]
  object C extends A[List[Double]] with AutoInject[A[List[Double]]]

  object TestA extends Injector {
    def test = {
      println(inject[A[List[Int]]]) // C
      println(inject[A[List[Double]]]) // C
    }
  }
```

## Utilities

### [ScalaTime](https://github.com/giiita/scaladia/blob/master/scaladia-container-core/src/main/scala/com/giitan/lang/ScalaTime.scala)

```scala
import com.giitan.lang.ScalaTime._
```
Supports conversion between various classes and date classes.

```scala
"2019-01-01 11:22:33".datetime
  
> res0: java.time.ZonedDateTime = 2019-01-01T11:22:33.000+09:00[Asia/Tokyo]
```

```scala
"2019-01-01 11:22:33".datetime.maxToday
  
> res0: java.time.ZonedDateTime = 2019-01-01T23:59:59.000+09:00[Asia/Tokyo]
```

```scala
"2019-01-01 11:22:33".datetime.maxToday.minTohour
  
> res0: java.time.ZonedDateTime = 2019-01-01T23:00:00.000+09:00[Asia/Tokyo]
```