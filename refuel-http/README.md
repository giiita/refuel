# refuel-http

## Usage

```
libraryDependencies ++= Seq(
  "com.phylage"       %% "refuel-http" % "1.3.13",
  "com.typesafe.akka" %% "akka-stream" % "2.6.4",
  "com.typesafe.akka" %% "akka-http"   % "10.1.11"
)


````

refuel-http infers ActorSystem from DI container.
To use it, you need to index.

```scala
object bench.Main extends Injector {
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

val result: Future[FutureSearch.Response] =
  http[GET](s"http://localhost:80/")
    .header("auth", "abcde")
    .as[ResponseType]
    .map(_.value)
    .flatMap(FutureSearch.byValue)
    .run
```

HttpRequestSetting is always reflected when making a request.
When changing, AutoInject injection is possible.

