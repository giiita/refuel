## scaladia-lang

```
libraryDependencies += "com.phylage" %% "scaladia-container" % "1.6.0"
```

### [ScalaTime](https://github.com/giiita/scaladia/blob/master/scaladia-lang/src/main/scala/com/phylage/scaladia/lang/ScalaTime.scala)

```scala
import com.phylage.scaladia.lang.ScalaTime._
```
Supports conversion between various classes and date classes.

```scala
"2019-01-01 11:22:33".datetime
  
> res0: java.time.ZonedDateTime = 2019-01-01T11:22:33.000+09:00[Asia/Tokyo]
```

```scala
"2019-01-01 11:22:33".datetime.maxToday
  
> res0: java.time.ZonedDateTime = 2019-01-01T23:59:59.9999999+09:00[Asia/Tokyo]
```

Configurations such as timezone can be overwritten by creating a class that inherits RuntimeTZ.

```scala
object MyTZ extends AsiaTokyoTZ with AutoInject[RuntimeTZ]
```

<br/>
<br/>

### [FromTo](https://github.com/giiita/scaladia/blob/master/scaladia-lang/src/main/scala/com/phylage/scaladia/lang/period/FromTo.scala)

FromTo is interface that represents a period.
Classes that inherit this can use certain features.

```scala
case class RequestPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo

RequestPeriod("2019-08-01 00:00:00".datetime.epoch, "2019-08-31 23:59:59".datetime.epoch)
  .overlap(RequestPeriod("2019-07-25 00:00:00".datetime.epoch, "2019-07-31 23:59:59".datetime.epoch)
```

<br/>
<br/>

### [ScalaCollection](https://github.com/giiita/scaladia/blob/master/scaladia-lang/src/main/scala/com/phylage/scaladia/lang/collections/ScalaCollection.scala)

Collection extension functions.