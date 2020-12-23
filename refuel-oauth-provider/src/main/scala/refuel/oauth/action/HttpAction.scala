package refuel.oauth.action

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Location
import refuel.oauth.exception.{AuthorizeChallengeFailed, AuthorizeRejectionException}

trait HttpAction {
  def status: StatusCode

  def result: ToResponseMarshallable
}

object HttpAction {
  def apply(e: Throwable): HttpAction = e match {
    case e: AuthorizeRejectionException => UnauthorizedAction(e)
    case e: AuthorizeChallengeFailed    => RedirectAction(e)
    case e                              => UnauthorizedAction(e)
  }
}
