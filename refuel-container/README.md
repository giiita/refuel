# refuel-container

```
libraryDependencies += "com.phylage" %% "refuel-container" % "1.5.2"
````

## Features

In standard use, dependencies are resolved at compile time.
If the dependency definition is invalid, Compile will fail.

Runtime injection can also be specified. In this case, even if the dependency is not found at compile time, the error can be avoided.<br/>
In layered architectures, for example, primary constructor injection is recommended in order to avoid having dependency decisions made in submodules.

In sub module.
```scala
class MyService(myRepository: MyRepository, myClient: MyClient) extends AutoInject {
  def run = {
    myRepository.find().map(myClient.post)
  }
}
```

In root module.
```scala
class Main extends AutoInject with Injector {
  inject[MyService].run
}
```

This ensures that dependencies are not determined at build time for modules containing MyService. </BR>.
Dependency resolution is always done at the time of the `inject[T]` call, within the current compile time classpath.

However, the dependency is not always determined at the compile time.
Since refuel-container is a container based injection, before assigning the generated value, it searches for an instance with the same characteristics among the container instances that can be referenced at that time.

If you want to always search for the latest dependency from the functions that the instance has, you can wrap the constructor parameter with `Lazy[T]` to always search for T. However, this is a process that is done only once when the primary constructor is applied. (This does not create much overhead).

```scala
// myRepository will be searched in the container each time it is used.
class MyService(myRepository: Lazy[MyRepository], myClient: MyClient) extends AutoInject {
  def run = {
    myRepository.find().map(myClient.post)
  }
}

class MyRepositoryImpl extends MyRepository
```

# Container mirroring

`shade` and `closed` can be used to mirror the current scope.

### `shade[T](func: Container => T)`

shade creates a duplicate of the container in the current scope.
The purpose of using this function is to define a temporary dependency.

Normally, dynamic dependency registration may be used for other uninitialized injections.
If you want to inject a higher priority instance than usual only in the temporary scope, you can register it in the shade function to define the dependency without affecting the container in the public scope.

```scala
class MyRepositoryOverrides extends MyRepository

class AAA(myService: MyService) extends AutoInject {
  myService.run // Outside the shade scope, myService would inject MyRepositoryImpl in its constructor
  
  shade { implicit c =>
    // However, if it is in the shade function scope, it will be registered
    // in a temporarily duplicated container and will not be registered in
    // the public scope container.

    // If MyService#MyRepository is not wrapped in Lazy, MyRepository will
    // be fixed in MyRepositoryImpl by applying constructor, so be careful 
    // with "the same inject root". 
    new MyRepositoryOverrides().index(Overwrite)
    myService.run // It will used MyRepositoryOverrides
  }
}
```

Note that shade is only a function scope that supports dynamic overrides, and dependency registrations resolved by refuel will propagate to the public scope container.


### `closed[T](func: Container => T)`

The usage of closed is the same, but the treatment of containers changes. closed, as the name implies, is a declaration that uses a container that has been completely denied blood.

In shade, only dynamic dependency registration is a separate action, and all other searches from automatic index/container are propagated to the public scope container.

This can be very useful when running many test cases in parallel.
In the case of parallelism, you don't know when and which dependencies will be registered in the public scope container, so the test results may change depending on the timing of execution, and the dependencies to be injected may change.

Since closed can prevent this from happening, it is recommended to use it whenever you implement tests.

```scala
class MyRepositoryOverrides extends MyRepository

class AAA(myService: MyService) extends AutoInject {
  myService.run // Outside the shade scope, myService would inject MyRepositoryImpl in its constructor
  
  closed { implicit c =>
    new MyRepositoryOverrides().index(Overwrite)
    myService.run // It will used MyRepositoryOverrides
  }
}
```
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
[info] <macro>:1:118: refuel.BarImpl will be used.
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
// This is the lowest priority
@Inject[Finally]
object A extends X with AutoInject

// This is the default priority
object B extends X with AutoInject

// This is the highest priority
@Inject[Primary]
object C extends X with AutoInject
```

If you want to control multiple overlapping dependencies, there are several approaches.

For a list of InjectionPriorities, see object InjectionPriority.


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
