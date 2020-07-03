package refuel.http.io.task
import akka.actor.ActorSystem

import scala.concurrent.Future

abstract class CombineTask[T] extends HttpTask[T] { me =>

  /** To synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  override def map[R](func: T => R): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.map(func)(as.dispatcher)
  }

  /**
    * To flatten synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  override def flatMap[R](func: T => HttpTask[R]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.flatMap(func(_).run)(as.dispatcher)
  }

  override def recover[R >: T](f: PartialFunction[Throwable, R]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] =
      me.run.recoverWith[R](f.andThen(x => Future.apply(x)(as.dispatcher)))(as.dispatcher)
  }
  override def recoverWith[R >: T](f: PartialFunction[Throwable, HttpTask[R]]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.recoverWith[R](f.andThen(_.run))(as.dispatcher)
  }
  override def recoverF[R >: T](f: PartialFunction[Throwable, Future[R]]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.recoverWith[R](f)(as.dispatcher)
  }
}
