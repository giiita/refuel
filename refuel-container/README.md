# refuel-container

```
libraryDependencies += "com.phylage" %% "refuel-container" % "1.3.2"
````

## Features

In most cases, refuel-container injections are inspected at compile time. If no dependency is found, Compile will fail. However, there is an injection option to switch to Runtime injection. In this case, the error can be avoided even if the dependency is not found at compile time.


# Usage

The general DI method only inherits `AutoInject` to the target object.<br/>
To use it, inherit `Injector` and call `inject[T]`.<br/>


```scala
class A extends AutoInject {
  def toString: String = "TEST"
}

object TestA extends Injector {
  println(inject[A].toString) // "TEST"
}
```

<br/>
<br/>
Dependencies determined by compiling are output to the compile log.
The same is true for the automatic injection of constructors.

```
[info] Foo.scala:266:26: refuel.FooImpl will be decided.
[info]       inject[Foo]
[info]
[info] <macro>:1:118: refuel.BarImpl will be decided.
[info] new refuel.FooImpl(inject[refuel.Bar]) with refuel.injector.Injector
```

<br/><br/><br/>

## Dependency handling

Classes that inherit AutoInject are automatically pooled.<br/>
Dependencies defined as inner objects of trait or class can not be registered automatically.<br/>
<br/>

In AutoInjector, if multiple dependencies of the same type are registered, will automatically insert the highest priority module.
If the same priority is found more than once, a compile error will occur.
The priority is set by `@Inject` annotation. If you don't set it, it will be default, but in most cases you won't need to set it.

```scala
trait X

@Inject(Finally)
object A extends X with AutoInject

// This is the default priority
object B extends X with AutoInject

// This is the highest priority
@Inject(Primary)
object C extends X with AutoInject

```


If you want to control multiple overlapping dependencies, there are several approaches.

### case 1. Tagging injection

You can define a dependency with any tag.

```scala
trait AliasA
trait AliasB
trait AliasC

trait A {
  def toString: String
}

object A extends A with Tag[AliasA] with AutoInject {
  override def toString: String = "I am A."
}
object B extends A with Tag[AliasB] with AutoInject {
  override def toString: String = "I am B."
}
object C extends A with Tag[AliasC] with AutoInject {
  override def toString: String = "I am C."
}

```

```scala
object TestA extends Injector {
  // A and the class that inherits AutoInject cannot be compiled because it has a duplicate priority.
  println(inject[A].toString)
  
  // "I am A."
  println(inject[A @@ AliasA].toString)
  
  // "I am B."
  println(inject[A @@ AliasB].toString) // "I am B."
}
```

<br/><br/>

## case 2. Effective injection

If you want to switch dependencies according to your environment or other conditions, you can use Effective Injection.

For example, load a different `Conf` for each `dev` / `stg` / `prd`. 

```scala
object Effects {
  def getKind = sys.props.getOrElse("env", "dev")
  
  case object DEV extends refuel.effect.Effect {
    def activate: Boolean = getKind == "dev"
  }
  case object STG extends refuel.effect.Effect {
    def activate: Boolean = getKind == "stg"
  }
  case object PRD extends refuel.effect.Effect {
    def activate: Boolean = getKind == "prd"
  }
}
```

```scala
@Effective(DEV)
object DevConf extends Conf with AutoInject

@Effective(STG)
object StgConf extends Conf with AutoInject

@Effective(PRD)
object PrdConf extends Conf with AutoInject
```

By this, an appropriate Conf is assigned to `inject[Conf]` for each environment.

It is evaluated in order of injection priority > effect, so it injects the dependencies that have the highest priority and have a valid effect. If none of the dependencies has a valid effect, the non-effect dependencies will be assigned with the same priority as those dependencies. If it doesn't even exist, an exception will be thrown.

###caution

Effective injection is always sublimated to runtime injection.
At the Macro expantion, it is not possible to evaluate the content of the Effect.

<br/><br/>

## Injection of non-existent dependencies in the compilation classpath

In a normal injection, if a candidate dependency is not found at the Macro expantion stage, you cannot compile it, but you may want to achieve it for the convenience of the software architecture.

<br/>

There are projects application, domain, and kernel.

【 PROJECT kernel 】
```scala
trait Interface {
  val value: String
}
```

【 PROJECT domain 】

```scala
class UseInterface extends Injector {
  // It can't compile.
  // Interface implementation cannot be found.
  def exec: String = inject[Interface].value
}
```

【 PROJECT application 】

```scala
object InterfaceImpl extends Interface with AutoInject {
  val value: String = "I am InterfaceImpl"
}

object Main extends App with Injector {
  // It will be fail.
  // It should be able to compile itself. However, the compilation fails because DOMAIN is failing to compile.
  println(inject[UseInterface].exec)
}
```

To resolve this, either suppress the explicit deployment at UseInterface or switch to RuntimeInjection.

### Suppress the explicit deployment at UseInterface

```scala
class UseInterface(interface: Interface) extends Injector {
  def exec: String = interface.value
}

object Main extends App with Injector {
  println(inject[UseInterface].exec)
}
```

This allows the project application only to be deployed in this way.

```scala
println(inject[UseInterface].exec)
// STEP1
println(UseInterface(inject[Interface]).exec)
// STEP2
println(UseInterface(InterfaceImpl).exec)
```

### Switch to RuntimeInjection

By adding `@RecognizedDynamicInjection` to the type passed to the inject function, the injection for that class will be a Runtime injection.

```scala
@RecognizedDynamicInjection
trait Interface {
  val value: String
}
```


However, in this way, the injection of `Interface` is always Runtime.
Use `inject[T@RecognizedDynamicInjection]` if you want to switch to Runtime only for some calls.
This can also be used for reserved type classes.

```scala
class UseInterface extends Injector {
  def exec: String = inject[Interface@RecognizedDynamicInjection].value
}
```


<br/>

### Testing

When UnitTest parallel execution is enabled, overriding global scope dependencies such as `overwrite` in a test may result in unexpected overwrites between different threads.<br/>
There are two cases for dealing with the problem.<br/>

### case 1. Narrow indexing

Call `narrow` to create Indexer, set access permission list and register to DI container.<br/>
As a result, only TargetServiceImpl can inject MockA.<br/>
These can be registered multiple times, and in addition to objects, access from specific classes can be permitted.


```scala
  val targetService = TargetServiceImpl
  narrow[A](new MockA).accept(targetService).indexing()
  targetService.exec() // MockA is used for A in targetService
```

### case 2. Container shading

Classes that inherit Injector can call the `shade` function.<br/>
Calling this will create a temporary mock container.<br/>
In this closed area, global dependency registration (overwrite) does not affect the standard container.<br/>
This eliminates the need to propagate overriding dependency overrides.<br/>


```scala
trait A {
  val value
}
object AImpl extends A with AutoInject {
  val value = "I am AImpl"
}

trait B extends Injector {
  val a = inject[A]
}
object B extends B with AutoInject

trait C extends Injector {
  val b = inject[B]
}
object C extends C with AutoInject


object Test extends App with Injector {
  println(inject[C].b.a.value) // Will be "I am AImpl"
  
  println(
    shade { implicit c =>
      overwrite[A](
        new A {
          val value = "I am new A"
        }
      )
      inject[C].b.a.value // Will be "I am new A"
    }
  )
  
  println(inject[C].b.a.value) // Will be "I am AImpl"
}

```

<br/>
<br/>
<br/>
<br/>

# Function index

### Override dependency

```scala
object A extends AutoInject

trait A {
  def toString: String = "TEST"
}
```

```scala
object TestA extends Injector {
  private[this] val a: A = inject[A]

  overwrite[A] {
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

### This will not work

```scala
  trait A
  object B extends A
  object C extends A
  
  trait X
  object Y extends X
  object Z extends X
  
  trait BInjector extends Injector {
    overwrite[A](B)
  }
  
  trait ZInjector extends Injector {
    overwrite[X](Z)
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
    overwrite[A](B)
  }
  
  trait CInjector extends Injector {
    overwrite[A](C)
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
  object B extends A[List[String]] with AutoInject
  object C extends A[List[BigDecimal]] with AutoInject

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
  object B extends A[List[Int]] with AutoInject
  object C extends A[List[Double]] with AutoInject

  object TestA extends Injector {
    def test = {
      println(inject[A[List[Int]]]) // C
      println(inject[A[List[Double]]]) // C
    }
  }
```
