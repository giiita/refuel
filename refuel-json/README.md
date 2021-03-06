# refuel-json

```
libraryDependencies += "com.phylage" %% "refuel-json" % "1.5.2"
```

refuel-json automatically generates codec and supports JSON mutual conversion fast and easy.

```
[info] Benchmark                      Mode  Cnt   Score    Error  Units

[info] Deserialize.runJson4sNative    avgt   10   1.276 ±  0.021  ms/op
[info] Deserialize.runPlayJson        avgt   10   1.202 ±  0.017  ms/op
[info] Deserialize.runJson4sJackson   avgt   10   1.181 ±  0.064  ms/op
[info] Deserialize.runArgonautJson    avgt   10   0.846 ±  0.029  ms/op
[info] Deserialize.runRefuelParsing   avgt   10   0.491 ±  0.017  ms/op <<
[info] Deserialize.runCirce           avgt   10   0.339 ±  0.026  ms/op
[info] Deserialize.runSphereJson      avgt   10   0.316 ±  0.008  ms/op
[info] Deserialize.runUJson           avgt   10   0.293 ±  0.004  ms/op
[info] Deserialize.runSprayJson       avgt   10   0.258 ±  0.004  ms/op
[info] Deserialize.runJacksonParsing  avgt   10   0.182 ±  0.002  ms/op
[info] Deserialize.runJsoniter        avgt   10   0.091 ±  0.001  ms/op

[info] Serialize.runJson4sJackson     avgt   10   0.874 ±  0.021  ms/op
[info] Serialize.runJson4sNative      avgt   10   0.798 ±  0.006  ms/op
[info] Serialize.runArgonautJson      avgt   10   0.796 ±  0.051  ms/op
[info] Serialize.runPlayJson          avgt   10   0.614 ±  0.020  ms/op
[info] Serialize.runCirce             avgt   10   0.425 ±  0.030  ms/op
[info] Serialize.runSprayJson         avgt   10   0.354 ±  0.006  ms/op
[info] Serialize.runRefuelParsing     avgt   10   0.263 ±  0.009  ms/op <<
[info] Serialize.runSphereJson        avgt   10   0.254 ±  0.003  ms/op
[info] Serialize.runUJson             avgt   10   0.225 ±  0.002  ms/op
[info] Serialize.runJacksonParsing    avgt   10   0.078 ±  0.001  ms/op
[info] Serialize.runJsoniter          avgt   10   0.060 ±  0.004  ms/op
```

## Usage

Generate any codec with CaseClassCodec or ConstCodec.<br/>
Both CaseClassCodec and ConstCodec are automatically generated up to the member's internal Codec.

To use them, inherit from `refuel.json.CodecDef`. `refuel.json.JsonTransform` is required to perform the transformation.

```scala
class Test extends JsonTransform with CodecDef {
  // A and B codecs do not need to be declared
  jsonString.as(CaseClassCodec.from[C])
}

case class A(value: String)
case class B(a: A)
case class C(b: B)
```

However, you may want to change the lower codec when generating the upper codec.<br/>
In that case, if an implicit Codec exists in the implicit scope, it will be substituted.

```scala
case class A(value: String)
case class B(a: A)
case class C(b: B)
case class D(c: C)

implicit val B_CODEC: Codec[B] = ???

// In this case, B_CODEC is used internally.
jsonString.as(CaseClassCodec.from[D])
```

`CaseClassCodec.from[T]` generates a Codec only for classes with `apply` and `unapply/unapplySeq`.<br/>
Use ConstCodec if you do not have apply / unapply, such as trait or Factory function.<br/>
Codec is generated by explicitly passing json key name and `apply` / `unapply`.

```scala
trait A {
  val id: String
  val value: Int
}


// In this case, B_CODEC is used internally.
s"""{"id": "abcde", "value_number": 123}""".as(
  ConstCodec.from("id", "value_number")((a, b) => new A {
    val id = a
    val value = b
  })(z => Some((z.id, z.value))))
```

Similarly, ConstCodec does not need to declare the inner class Codec.<br/>
In addition, the value class that inherits AnyVal is converted in the state of being internally expanded.

```scala
case class StringVal(value: String) extends AnyVal
case class LongVal(value: Long) extends AnyVal
case class A(str: StringVal, lng: LongVal)

// {"str": "foo", "lng": 11}
A(StringVal("foo"), LongVal(11)).toJString(CaseClassCodec.from[A])

// A(StringVal("foo"), LongVal(11))
"""{"str": "foo", "lng": 11}""".as(CaseClassCodec.from[A])
```

If you do not need flat expansion, you need to define codec in implicit scope by yourself.

```scala
implicit val StringValCodec = ConstCodec.from("value")(StringVal.apply)(StringVal.unapply)
implicit val LongValCodec = ConstCodec.from("value")(LongVal.apply)(LongVal.unapply)
```

## Codec build DSL

It is possible to build arbitrary Codec by combining specific Codec.

```
{
  "area1": {
    "parent1": [
      {
        "childId": 1,
        "props": ["xxx", "yyy"]
      },
      {
        "childId": 2,
        "props": ["aaa"],
        "ability": ["???"]
      }
    ],
    "parent3": [
      {
        "childId": 3,
        "props": [],
        "ability": ["???", "???", "???"]
      },
      {
        "childId": 4,
        "props": ["aaa"],
      }
    ]
  }
}
```

```scala
  val parentsCodec = (
    option("parent1".parsed(vector(ChildCodec))) ++
    option("parent1".parsed(vector(ChildCodec))) ++
    option("parent1".parsed(vector(ChildCodec)))
  )(Parents.apply)(Parents.unapply)

  val rootCodec = (
    "area1".parsed(option(parentsCodec)) ++
    "area2".parsed(option(parentsCodec)) ++
    "area3".parsed(option(parentsCodec))
  )(Root.apply)(Root.unapply)

  val wrapExample = "root".extend(rootCodec)
```

In this way, codecs can be generated according to JsonFormat, domain model, etc.

## Dynamic codec creation

Codec by macro cannot be generated conditional on dynamic values.
It will be inconvenient to consider polymorphism.

```scala 
sealed abstract class Animal
case class Cat(name: String, beard: Int = 6) extends Animal
case class Shark(name: String, filet: Int = 4) extends Animal

// This cannot be compiled due to the nature of macro expansion
def animalCodec(v: Animal): Codec[Animal] = v match {
  case _: Cat   => ContsCodec.from(v.name)(x => Cat(v.name))(x => Some(x))
  case _: Shark => ContsCodec.from(v.name)(x => Shark(v.name))(x => Some(x))
}
```

To work around this, use dynamic codec creation.

```scala
trait AnimalCodec extends CodecDef {
  sealed abstract class Animal(name: String)
  case class Cat(name: String, beard: Int = 6) extends Animal(name)
  case class Shark(name: String, filet: Int = 4) extends Animal(name)
  
  def animalDeserializer: Read[Animal] = Deserialize { json =>
    json.named("name") match {
      case JsString("cat") => Cat("cat", json.named("beard").to[Int])
      case JsString("shark") => Shark("shark", json.named("filet").to[Int])
    }
  }
  def animalSerializer: Write[Animal] = Serialize {
    case Cat(a, b) => Json.obj(
      "name" -> a,
      "beard" -> b
    )
    case Shark(a, b) => Json.obj(
      "name" -> a,
      "filet" -> b
    )
  }
  implicit def animalCodec: Codec[Animal] = Format(animalDeserializer)(animalSerializer)
} 
```

In the case of deserlialize-only codecs, codecs can be synthesized by readMap.

```scala
Deserialize(_.named("name").des[String]).readMap {
  case "cat" => Deserialize(_.named("beard").des[Int])
  case "shark" => Deserialize(_.named("filet").des[Int])
}
```


Reusing an existing codec and adding a specific json key.

```scala
val animalCodec: Codec[Animal] = ???

val animalResponseWrites: Write[Animal] = WriteWith[Animal]("result")(animalCodec)

// It will be
// {
//   "result": {
//     ... (animalCodec serialization)
//   }
// }
```

Others are `ReadWith[T: Read]` and `BothWith[T: Codec]`.
You can also specify a nested key.

```scala
WriteWith[Animal]("result" @@ "animal")(animalCodec)
```

```json
{
  "result": {
    "animal": {
      ...
    }
  }
}
```