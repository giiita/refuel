# refuel-http

## Usage

```
libraryDependencies ++= Seq(
  "com.phylage"       %% "refuel-http" % "1.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.6.4",
  "com.typesafe.akka" %% "akka-http"   % "10.1.11"
)


````

refuel-http infers ActorSystem from DI container.
To use it, you need to index.

```scala
object Main extends Injector {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem().index()
  }
}
```

## Examples

### HTTP Server call

```
import refuel.http.io.Http._

object MySetting extends HttpRequestSetting(retryThreshold = 3) with AutoInject

val requets = Map(
  "id" -> 1,
  "name" -> "Jack"
)

val result: Future[FutureSearch.Response] =
  http[GET](s"http://localhost:80/?${requets.asUrl}")
    .header("auth", "abcde")
    .as[ResponseType]
    .map(_.value)
    .flatMap(FutureSearch.byValue)
    .run
```

HttpRequestSetting is always reflected when making a request.
When changing, AutoInject injection is possible.

