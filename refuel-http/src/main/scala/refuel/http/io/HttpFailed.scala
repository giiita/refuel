package refuel.http.io

import akka.http.scaladsl.model.{HttpResponse, StatusCode}

sealed class HttpFailed[T](val entry: T) extends Throwable

case class HttpRequestFailed(status: StatusCode, override val entry: HttpResponse)
    extends HttpFailed[HttpResponse](entry) {
  override def getMessage: String = s"Http request failed. status code: ${status.value}"
}
case class HttpProcessingFailed(override val entry: Throwable) extends HttpFailed[Throwable](entry) {
  override def getMessage: String = entry.getMessage
}
