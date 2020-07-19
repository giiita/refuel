package refuel.http.io

import com.typesafe.scalalogging.Logger
import refuel.injector.Injector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class HttpRetryRevolver(maxRetry: Int) extends Injector {

  private val logger: Logger = Logger(classOf[HttpRetryRevolver])

  /**
    * Http request executor with retry.
    *
    * @param retry Max retry count.
    * @param func  Execution.
    * @tparam R Return type.
    * @return
    */
  final def revolving[R](retry: Int = 1)(func: => Future[R]): Future[R] = {
    func.recoverWith {
      case x if retry >= maxRetry =>
        logger.error(s"Request retry failed.", x)
        Future.failed(x)
      case x =>
        logger.warn(s"Request retry failed. ${x.getMessage}")
        revolving(retry + 1)(func)
    }
  }
}
