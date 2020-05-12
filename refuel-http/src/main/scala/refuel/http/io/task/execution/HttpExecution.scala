package refuel.http.io.task.execution

import akka.actor.ActorSystem

import scala.concurrent.Future

trait HttpExecution[E, T] {
  def execute(in: E)(implicit as: ActorSystem): Future[T]
}
