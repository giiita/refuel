package refuel.http.io.task.execution

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future

trait HttpResultExecution[T] extends HttpExecution[HttpRequest, T] {
  def execute(request: HttpRequest)(implicit as: ActorSystem): Future[T]
}
