# refuel-container

```
libraryDependencies += "com.phylage" %% "refuel-container" % "3.0.0"
````

# Common Usage

The general DI method only inherits `AutoInject[T]` to the target object.<br/>
To use it, inherit `Injector` and call `inject[T]`.<br/>
There is no restriction on calling `inject[T]`.


```scala
object A extends A with AutoInject[A]

trait A {
  def toString: String = "TEST"
}

object TestA extends Injector {
  println(inject[A].toString) // "TEST"
}
```

<br/>

`inject[T]` expects `Lazy[T]` as the return type.
<br>However, if you fix the return type of `inject[T]` to `T`, dependencies are assigned immediately; once initialization is complete, it cannot be changed.
<br>Therefore, it is not recommended.

```scala
object TestA extends Injector {
  // Dependencies are determined when object TestA is initialized
  private val x: A = inject[A]
  // This expects `Lazy[A]`
  private val y = inject[A]
}
```

<br/>
Instread, You can confirm dependencies at compile time with `confirm[T]`.<br/>
In this case, anything that is not forseen happens at runtime.<br/>

```scala
object TestA extends Injector {
  // This expects `A`
  private val x = confirm[A]
}
```

<br/><br/><br/>

## Dependency handling

Classes that inherit AutoInjector are automatically pooled.<br/>
Dependencies defined as inner objects of trait or class can not be registered automatically.<br/>
<br/>

In AutoInjector, if multiple dependencies of the same type are registered, refuel will automatically insert the highest priority first deployed module.

```scala
object A extends X with RecoveredInject[X]
object B extends X with AutoInject[X]
// This is the highest priority
object C extends AutoInjectCustomPriority[X](9999) with X

trait X
```


If you want to control multiple overlapping dependencies, there are several approaches.

### case 1. Custom priority injection

Each injection interface has a priority.<br/>
During injection, the highest priority object registered in the container is returned, 
so set priorities appropriately with multiple same interfaces.<br/>
You can also create an `AutoInject[_]` interface with any priority by mixing in `CustomPriority[T]`, which inherits from `AutoInject[T]`.

```scala
/**
 * AutoInject priority = 1000
 * Injector.overwrite[X](x) = 1100
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

If you want to inject object only with arbitrary tags, you can use the tag `@@`.
<br/>
For example, an object that inherits from `AutoInject[T @@ TAG]` will not be injected unless you call `inject[T @@ TAG]`.

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
  println(inject[A].toString) // "I am C.". Because refuel recognizes objects that inherit tags as different objects
  
  println(inject[A @@ AliasA].toString) // "I am A."
  
  println(inject[A @@ AliasB].toString) // "I am B."
}
```

<br/><br/>

## Effective injection

If you want to load an specific object from different environment, you can use Effective Injection.


For example, if you want to load a different `Conf` for each `dev` / `stg` / `prd` and the environment setting is set to `sys.props`, prepare an Effect to judge it for each environment.

```scala
object Effects {
  def getKind = sys.props.getOrElse("env", "dev")
  
  object DEV extends refuel.effect.Effect {
    def activate: Boolean = getKind == "dev"
  }
  object STG extends refuel.effect.Effect {
    def activate: Boolean = getKind == "stg"
  }
  object PRD extends refuel.effect.Effect {
    def activate: Boolean = getKind == "prd"
  }
}
```

Then, declare the object that gave the effect to `@Effective`

```scala
@Effective(DEV)
object DevConf extends Conf with AutoInject[Conf]

@Effective(STG)
object StgConf extends Conf with AutoInject[Conf]

@Effective(PRD)
object PrdConf extends Conf with AutoInject[Conf]
```

This will assign an appropriate Conf for each environment to `inject[Conf]`.
<br>You can check the valid Effect by seeing the result of `def activate: Boolean`.

When you call `inject[???]` and `@Effective(???)` appears as an assignment candidate,
the `inject[???]` is considered as an effective injection, and only objects with a valid effect can be assigned.


<br/><br/>

## Injection across modules / projects

Dependencies that can be referenced from the runtime classpath are always loaded.
Such an injection will also work as expected.

Here is an example.
https://github.com/giiita/refuel/tree/master/test-across-module

<br/>

When there are projects A, B and C, C depends on B, B depends on A.<br/>

【 PROJECT A 】
```scala
trait N {
  val value = "I am N in project A."
}
```

【 PROJECT B 】

```scala
trait UseN extends Injector {
  def exec: String = inject[N].value
}
object UseN extends UseN with AutoInject[UseN] 
```

【 PROJECT C 】

```scala
object NImpl extends N with AutoInject[N] {
  val value = "I am NImpl in project C."
}

object Main extends App with Injector {
  println(UseN.exec) // "I am NImpl in project C."
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

### case 2. Container shadind

Classes that inherit Injector can call the `shade` function.<br/>
Calling this will create a temporary mock container.<br/>
In this closed area, global dependency registration (overwrite) does not affect the standard container.<br/>
This eliminates the need to propagate overriding dependency overrides.<br/>


```scala
trait A {
  val value
}
object AImpl extends A with AutoInject[A] {
  val value = "I am AImpl"
}

trait B extends Injector {
  val a = inject[A]
}
object B extends B with AutoInject[B]

trait C extends Injector {
  val b = inject[B]
}
object C extends C with AutoInject[C]


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
object A extends AutoInject[A]

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


### Advanced usage

```scala
  trait A
  object B extends A
  object C extends A
  
  trait BInjector extends Injector {
    overwrite[A](B)
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
