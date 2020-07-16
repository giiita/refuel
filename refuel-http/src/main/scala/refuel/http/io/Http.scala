package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.util.ByteString
import refuel.container.Container
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.http.io.setting.HttpSetting
import refuel.http.io.setting.HttpSetting.RecoveredHttpSetting
import refuel.http.io.task.HttpTask
import refuel.http.io.task.execution.HttpResultExecution
import refuel.injector.{AutoInject, Injector}
import refuel.json.JsonTransform
import refuel.json.codecs.Read
import refuel.provider.Lazy

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.implicitConversions

@deprecated("Instead, use dependency injection")
@Inject(Finally)
object Http extends Http(new Lazy[HttpSetting] {
  override def _provide(implicit ctn: Container): HttpSetting = new RecoveredHttpSetting
}) {

}

/** {{{
  * class MyRepository(http: Http) extends AutoInject {
  *   import http._
  *
  *   http[GET]("http://???")
  *     .asString.run
  * }
  * }}}
  */
@Inject(Finally)
class Http(val setting: Lazy[HttpSetting]) extends Injector with JsonTransform with AutoInject {

  implicit def __setting: HttpSetting = setting

  implicit def toUri(uri: String): Uri = Uri(uri)

  /**
    * Create a http request task.
    * {{{
    *   val result: FutureSearch.Response =
    *     http[GET](s"http://localhost:80/?${requets.asUrl}".withQuery(Map("param" -> "value")))
    *     .header("auth", "abcde")
    *     .deserializing[ResponseType]
    *     .map(_.value)
    *     .flatMap(FutureSearch.byValue)
    *     .run
    * }}}
    *
    * All abnormal system statuses will be demoted to `HttpRequestFailed[HttpResponse]`.
    * If the retry limit is exceeded and it continues to fail, HttpProcessingFailed[Throwable] will occur.
    *
    * @param uri Request url
    * @tparam T Request method type. See [[refuel.http.io.HttpMethod]]
    * @return
    */
  def http[T <: HttpMethod.Method : MethodType](uri: Uri): HttpRunner[HttpResponse] = {
    new HttpRunner[HttpResponse](
      setting.requestBuilder(
        HttpRequest(implicitly[MethodType[T]].method).withUri(uri)
      ),
      new HttpResultExecution[HttpResponse] {
        def execute(request: HttpRequest)(implicit as: ActorSystem): Future[HttpResponse] =
          HttpRetryRevolver(setting.retryThreshold)
            .revolving() {
              akka.http.scaladsl.Http().singleRequest(request)
            }
            .flatMap { res =>
              if (res.status.isSuccess()) Future(res)(as.dispatcher)
              else Future.failed(HttpRequestFailed(res.status, res))
            }(as.dispatcher)
      }
    )
  }
}
