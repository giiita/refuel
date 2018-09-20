# scaladia-container-core

## How to use

```
libraryDependencies += "com.github.giiita" %% "scaladia" % "1.3.0"
````

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

### Inject with member of object


```
object A extends AutoInject[A]

trait A {
  def toString: String = "TEST"
}
```

```
object TestA extends Injector {
  private[this] val a: A = inject[A]

  def test = {
    println(a.toString) // TEST
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



AutoInject[T]を継承したオブジェクト Tは、DIコンテナの初期化時に自動的に登録されます。<br/>
Scaladiaのクラスローダーから依存関係を解決します。<br/>
ただし、object定義を自動注入する場合、静的にアクセスできる階層に定義しなければなりません。(case classなどの場合初期化できません。)<br/>
AutoInjectorでは、同じタイプの複数の依存関係が登録されている場合、どちらが注入されるかは保証されません。
その場合、Injectorを使用してください。
AutoInjectで注入した設定をInjectorで上書きすることができます。
AutoInjectは自己タイプを注入するためのインターフェースです。

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