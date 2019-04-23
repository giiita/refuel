# scaladia-container-core

## Usage

```
libraryDependencies += "com.github.giiita" %% "scaladia" % "1.6.0"
````

## Supports full scanning injection (v1.5.0 ~) (Default true from v1.6.0 ~)

Until now it was a premise to use it with FatJar (sbt-assembly).<br/>
AutoInjectScan was supported only from the archive that Scaladia package was shipped with.
However, since v1.5.0, AutoInjectScan is possible anywhere in the classpath.
In order to do that, you need to write a configuration file.

- src/main/resources/reference.conf
```
scaladia {
  fullscan = true
  whitelist = ["scaladia", "-mycompany-lib"]
}
```


If fullscan is set to true, all AutoInjects in the classpath will be loaded.<br/>
For whitelist, set loadable archives name (broad match).<br/>
If you omit the whitelist, you will load archives such as jre and scala-lang, so the performance immediately after starting Application will degrade.<br/>
Even if fullscan is false, AutoInject of the current class loader will be loaded.

It defaults to `true` from `1.6.0 ~`.

## Examples

### The simplest Injection

```
object A extends AutoInject[A]

trait A {
  def toString: String = "TEST"
}
```

```
object TestA extends Injector {
  def test = {
    println(inject[A].toString) // "TEST"
  }
}
```

### Constructor dependency injection 


```
object A extends AutoInject[A]

trait A {
  def exec: String = "TEST"
}
```

```
object TestA extends Injector {
  // Need not be lazy val
  // It is implicitly initialized when accessed for the first time
  // However, specifying a type is deprecated because it is initialized in the constructor.
  // ex:) `private val a: A = inject[A]` 
  private[this] val a = inject[A] // a's type is StoredDependency[A]

  def test = {
    println(a.exec) // TEST
  }
}
```

Classes that inherit AutoInjector are registered automatically when DI container is initialized.<br/>
Resolve dependencies from the class loader as seen from Scaladia 's static initializer.<br/>
However, when automatically injecting the object definition, it must be defined in a statically accessible hierarchy.<br/>
In AutoInjector, when multiple dependencies of the same type are registered, it is not guaranteed which one is injected.
Please use Injector in such a case.
You can overwrite settings injected with AutoInjector with Injector.<br/>
<br/>
Currently, AutoInjector is an interface for injecting self type.<br/>

Even if you set injection settings other than self type, it may not initialize to the correct order and may not behave as intended.<br/><br/><br/>

### Testing

When UnitTest parallel execution is enabled, overriding global scope dependencies such as `depends` in a test may result in unexpected overwrites between different threads.<br/>
Therefore, be careful to use narrowly overriding in unit tests.

```
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

```
object A extends AutoInject[A]

trait A {
  def toString: String = "TEST"
}
```

```
object TestA extends Injector {
  private[this] val a: A = inject[A]

  depends[A] {
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
```
narrow[T](new T).accept(this).indexing()
```
Overwriting with `depends` and running tests in parallel may unexpectedly overwrite globally.


### Custom usage

```
  trait A
  object B extends A
  object C extends A
  
  trait BInjector extends Injector {
    depends[A](B)
  }
  
  object Test extends BInjector {
    def test = {
      println(inject[A]) // B
    }
  }
  
```

### This will not work

```
  trait A
  object B extends A
  object C extends A
  
  trait X
  object Y extends X
  object Z extends X
  
  trait BInjector extends Injector {
    depends[A](B)
  }
  
  trait ZInjector extends Injector {
    depends[X](Z)
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

```
  trait A
  object B extends A
  object C extends A
  
  trait BInjector extends Injector {
    depends[A](B)
  }
  
  trait CInjector extends Injector {
    depends[A](C)
  }
  
  object TestA extends BInjector with CInjector {
    def test = {
      println(inject[A]) // C
    }
  }
```

### It also supports type erase.

```
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

```
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

```
import com.giitan.lang.ScalaTime._
```
Supports conversion between various classes and date classes.

```
"2019-01-01 11:22:33".datetime
  
> res0: java.time.ZonedDateTime = 2019-01-01T11:22:33.000+09:00[Asia/Tokyo]
```

```
"2019-01-01 11:22:33".datetime.maxToday
  
> res0: java.time.ZonedDateTime = 2019-01-01T23:59:59.000+09:00[Asia/Tokyo]
```

```
"2019-01-01 11:22:33".datetime.maxToday.minTohour
  
> res0: java.time.ZonedDateTime = 2019-01-01T23:00:00.000+09:00[Asia/Tokyo]
```