package refuel.oauth.action

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{HttpHeader, HttpResponse, StatusCode, StatusCodes}
import refuel.oauth.exception.AuthorizeChallengeFailed

case class BadRequestAction() extends HttpAction {
  def status: StatusCode = StatusCodes.BadRequest

  override def result: ToResponseMarshallable = status
}

object BadRequestAction {
  def apply(e: Throwable): BadRequestAction =
    new BadRequestAction()
}
