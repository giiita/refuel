package refuel.oauth.action

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{HttpHeader, HttpResponse, StatusCode, StatusCodes}
import refuel.oauth.exception.AuthorizeChallengeFailed

import scala.collection.immutable

case class RedirectAction(headers: Iterable[HttpHeader], cause: Option[Throwable] = None) extends HttpAction {
  override def result: ToResponseMarshallable = HttpResponse.apply(status, immutable.Seq.apply(headers.toSeq: _*))

  def status: StatusCode = StatusCodes.Found
}

object RedirectAction {
  def apply(e: AuthorizeChallengeFailed): RedirectAction =
    new RedirectAction(Some(Location(e.uri.withQuery(Query(e.toQueryMap)))), Some(e))
}
