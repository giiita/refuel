# refuel-http

## Usage

```
libraryDependencies += "com.phylage" %% "refuel-http" % "1.0.0"
````

## Examples

### HTTP Server call

```
import refuel.http.io.Http._

object MySetting extends HttpRequestSetting(retryThreshold = 3) with AutoInject[HttpRequestSetting]

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

