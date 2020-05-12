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
}
