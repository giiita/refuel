package refuel.oauth.action

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{StatusCode, StatusCodes}

case class UnauthorizedAction(cause: Throwable) extends HttpAction {
  def status: StatusCode = StatusCodes.Unauthorized

  override def result: ToResponseMarshallable = status
}
