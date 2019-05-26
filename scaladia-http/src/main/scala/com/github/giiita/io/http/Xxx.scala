package com.github.giiita.io.http

import Http._
import com.github.giiita.io.http.HttpMethod.POST

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object Xxx {

  def main(args: Array[String]): Unit = {
    val f = Future().transform {
      case scala.util.Success(_) => scala.util.Success {
        http[POST]("http://hooks.slack.com/test")
          .run
          .map(_ => {})
          .recover {
            case _ => {}
          }
      }
      case scala.util.Failure(exception) => throw exception
    }

    Await.result(f, Duration.Inf)
    println("PROCESS CLOSED")
  }
}
