package refuel.http.io

import akka.http.scaladsl.model.HttpResponse

sealed class HttpRequestFailed[T](val entry: T, cause: Throwable = null) extends RuntimeException(cause)

case class HttpResponseError[T](override val entry: T, response: HttpResponse) extends HttpRequestFailed[T](entry) {
  override def getMessage: String = s"Http request failed. status code: ${response.status.value}"
}

case class HttpErrorRaw(override val entry: HttpResponse, strictedSource: Option[String], cause: Throwable = null)
    extends HttpRequestFailed[HttpResponse](entry, cause) {
  override def getMessage: String = s"Http request failed. status code: ${entry.status.value}"
}
