package refuel.http.io

import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future

trait HttpResultTask[T] {
  def execute(request: HttpRequest): Future[T]
}
