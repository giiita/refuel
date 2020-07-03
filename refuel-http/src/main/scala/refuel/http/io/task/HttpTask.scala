package refuel.http.io.task

import akka.actor.ActorSystem

import scala.concurrent.Future

trait HttpTask[T] { me =>

  /**
    * To synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  def map[R](func: T => R): HttpTask[R]

  /**
    * To flatten synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  def flatMap[R](func: T => HttpTask[R]): HttpTask[R]

  /**
    * Execute future functions.
    *
    * @return
    */
  def run(implicit as: ActorSystem): Future[T]

  def recover[R >: T](f: PartialFunction[Throwable, R]): HttpTask[R]
  def recoverWith[R >: T](f: PartialFunction[Throwable, HttpTask[R]]): HttpTask[R]
  def recoverF[R >: T](f: PartialFunction[Throwable, Future[R]]): HttpTask[R]
}
