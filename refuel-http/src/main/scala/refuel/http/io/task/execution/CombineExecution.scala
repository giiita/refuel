package refuel.http.io.task.execution

import akka.actor.ActorSystem

import scala.concurrent.Future

trait CombineExecution[T] extends HttpExecution[Unit, T] {
  def execute(in: Unit)(implicit as: ActorSystem): Future[T]
}
