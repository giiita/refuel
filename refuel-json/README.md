# refuel-json

```
libraryDependencies += "com.phylage" %% "refuel-json" % "2.0.1"
```

refuel-json automatically generates codec and supports JSON mutual conversion fast and easy.
https://github.com/yanns/scala-json-parsers-performance

```
Benchmark                      Mode  Cnt   Score    Error  Units
Deserialize.runJson4sNative    avgt   10   0.947 ±  0.005  ms/op
Deserialize.runJson4sJackson   avgt   10   0.814 ±  0.003  ms/op
Deserialize.runArgonautJson    avgt   10   0.534 ±  0.007  ms/op
Deserialize.runPlayJson        avgt   10   0.424 ±  0.007  ms/op
Deserialize.runCirce           avgt   10   0.246 ±  0.002  ms/op
Deserialize.runSprayJson       avgt   10   0.244 ±  0.002  ms/op
Deserialize.runRefuelParsing   avgt   10   0.212 ±  0.004  ms/op <=
Deserialize.runWeePickle       avgt   10   0.149 ±  0.001  ms/op
Deserialize.runJacksonParsing  avgt   10   0.142 ±  0.001  ms/op
Deserialize.runUPickle         avgt   10   0.138 ±  0.001  ms/op
Deserialize.runBorer           avgt   10   0.104 ±  0.001  ms/op
Deserialize.runJsoniter        avgt   10   0.057 ±  0.001  ms/op

Benchmark                      Mode  Cnt   Score    Error  Units
Serialize.runJson4sNative      avgt   10   1.054 ±  0.006  ms/op
Serialize.runJson4sJackson     avgt   10   0.842 ±  0.009  ms/op
Serialize.runPlayJson          avgt   10   0.544 ±  0.033  ms/op
Serialize.runArgonautJson      avgt   10   0.362 ±  0.001  ms/op
Serialize.runRefuelParsing     avgt   10   0.273 ±  0.013  ms/op <=
Serialize.runCirce             avgt   10   0.236 ±  0.002  ms/op
Serialize.runSprayJson         avgt   10   0.235 ±  0.061  ms/op
Serialize.runUPickle           avgt   10   0.144 ±  0.082  ms/op
Serialize.runBorer             avgt   10   0.107 ±  0.001  ms/op
Serialize.runWeePickle         avgt   10   0.103 ±  0.012  ms/op
Serialize.runJacksonParsing    avgt   10   0.063 ±  0.001  ms/op
Serialize.runJsoniter          avgt   10   0.043 ±  0.001  ms/op
```

## Usage

Most Codecs can be auto-derived.

```scala
import JsonTransform._
class Test  {
  // A and B codecs do not need to be declared
  jsonString.readAs(Derive[C])
  // Scala3
  // jsonString.readAs(using Derive[C])
}

case class A(value: String)
case class B(a: A)
case class C(b: B)
```

If you want to customize a lower-level codec, declare/import the lower-level codec in the implicit scope.

```scala
case class A(value: String)
case class B(a: A)
case class C(b: B)
case class D(c: C)

implicit val BCodec: Codec[B] = ???

// In this case, B_CODEC is used internally.
jsonString.readAs(Derive[D])
```

`Derive[T]` generates a Codec only for classes with `apply` and `unapply/unapplySeq`.
Use `Deserialize[T]` / `Serialize[T]` / `Construct[T]` if you do not have apply / unapply, such as trait or Factory function.

```scala
trait A {
  val id: String
  val value: Int
}


// In this case, B_CODEC is used internally.
s"""{"id": "abcde", "value_number": 123}""".readAs(
  Deserialize[A] { json =>
    Try {
      new A {
        val id = json.named("id").readAs[String]
        val value = json.named("value_number").readAs[Int]
      }
    }
  }
)
```

Similarly, ConstCodec does not need to declare the inner class Codec.
In addition, the value class that inherits AnyVal is converted in the state of being internally expanded.

```scala
case class StringVal(value: String) extends AnyVal
case class LongVal(value: Long) extends AnyVal
case class A(str: StringVal, lng: LongVal)

// {"str": "foo", "lng": 11}
A(StringVal("foo"), LongVal(11)).writeAsString(Derive[A])

// A(StringVal("foo"), LongVal(11))
"""{"str": "foo", "lng": 11}""".readAs(Derive[A])
``````