package refuel.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods
import refuel.http.io.HttpMethod._
import refuel.http.io.task.{CombineTask, HttpTask}

import scala.concurrent.Future

package object io {

  class MethodType[T](val method: akka.http.scaladsl.model.HttpMethod)

  implicit case object TRACE   extends MethodType[TRACE](HttpMethods.TRACE)
  implicit case object CONNECT extends MethodType[CONNECT](HttpMethods.CONNECT)
  implicit case object HEAD    extends MethodType[HEAD](HttpMethods.HEAD)
  implicit case object OPTIONS extends MethodType[OPTIONS](HttpMethods.OPTIONS)
  implicit case object PATCH   extends MethodType[PATCH](HttpMethods.PATCH)
  implicit case object GET     extends MethodType[GET](HttpMethods.GET)
  implicit case object PUT     extends MethodType[PUT](HttpMethods.PUT)
  implicit case object POST    extends MethodType[POST](HttpMethods.POST)
  implicit case object DELETE  extends MethodType[DELETE](HttpMethods.DELETE)

  implicit def map[T, R](f: ActorSystem => T => Future[R]): T => HttpTask[R] =
    res =>
      new CombineTask[R] {
        override def run(implicit as: ActorSystem): Future[R] = f(as)(res)
      }
}
