package refuel.http.io.setting

import akka.http.scaladsl.model.{HttpEntity, HttpProtocols, HttpRequest}
import refuel.domination.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

/**
  * Settings that are always reflected when making a request.
  * When changing, AutoInject injection is possible.
  *
  * {{{
  *   object MySetting extends HttpRequestSetting(retryThreshold = 3) with AutoInject[HttpRequestSetting]
  * }}}
  *
  * @param retryThreshold  Retry threshold; if 2 is set, a maximum of two attempts will be made.
  *                        Akka's standard try count is the number of attempts for HTTP communication,
  *                        but this setting detects the failure of the entire HttpTask and tries again.
  * @param requestBuilder  Http request builder.
  *                        Default set,
  *                        protocol: `Http/1.1`
  *                        ContentType: `application/json`
  * @param responseBuilder Http response entity builder.
  *                        For example, AkkaHttpClient has a capacity for response processing stream size.
  *                        It is possible to cut this.
  * {{{
  *   responseBuilder = _.withoutSizeLimit()
  * }}}
  */
class HttpSetting(
    val retryThreshold: Int = 1,
    val requestBuilder: HttpRequest => HttpRequest = HttpSetting.DEFAULT,
    val responseBuilder: HttpEntity.Strict => HttpEntity.Strict = x => x
)

object HttpSetting {

  val DEFAULT: HttpRequest => HttpRequest = _.setProtocol

  private[setting] implicit class RichHttpRequest(request: HttpRequest) {
    def setProtocol: HttpRequest = request.withProtocol(HttpProtocols.`HTTP/1.1`)
  }

  @Inject[Finally]
  class RecoveredHttpSetting() extends HttpSetting() with AutoInject

}
