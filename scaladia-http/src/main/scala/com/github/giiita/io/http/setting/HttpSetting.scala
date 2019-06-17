package com.github.giiita.io.http.setting

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpProtocol, HttpRequest}
import akka.stream.ActorMaterializer

/**
  * Settings that are always reflected when making a request.
  * When changing, AutoInject injection is possible.
  *
  * {{{
  *   object MySetting extends HttpRequestSetting(retryThreshold = 3) with AutoInject[HttpRequestSetting]
  * }}}
  *
  * @param retryThreshold    Retry threshold. When 2 is set, up to one failure is allowed.
  * @param build             Http request builder.
  * @param timeout           Request timeout milliseconds.
  * @param actorSystem       Actor system.
  * @param actorMaterializer Actor materializer generator.
  */
class HttpSetting(val retryThreshold: Int = 1,
                  val build: HttpRequest => HttpRequest = HttpSetting.DEFAULT,
                  val timeout: Int = 30 * 1000,
                  val actorSystem: ActorSystem = ActorSystem(),
                  val actorMaterializer: ActorSystem => ActorMaterializer = x => ActorMaterializer()(x))

object HttpSetting {

  val DEFAULT: HttpRequest => HttpRequest = _.setProtocol.setContentType

  private[setting] implicit class RichHttpRequest(request: HttpRequest) {
    def setProtocol: HttpRequest = request.withProtocol(HttpProtocol("HTTP"))

    def setContentType: HttpRequest = request.withHeaders(RawHeader("Content-type", "application/json"))
  }

}