package com.phylage.scaladia.http.io

import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future

trait HttpResultTask[T] extends JacksonParser {
  def execute(request: HttpRequest): Future[T]
}
