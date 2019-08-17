package com.phylage.scaladia.http.io

import com.phylage.scaladia.injector.Injector
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


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
        throw x
      case x =>
        logger.warn(s"Request retry failed. ${x.getMessage}")
        revolving(retry + 1)(func)
    }
  }
}
