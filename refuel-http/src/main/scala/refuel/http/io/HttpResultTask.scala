package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future

trait HttpResultTask[T] {
  def execute(request: HttpRequest)(implicit as: ActorSystem): Future[T]
}
