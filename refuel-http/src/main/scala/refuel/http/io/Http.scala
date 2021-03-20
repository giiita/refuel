package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import com.typesafe.scalalogging.LazyLogging
import refuel.container.Container
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.http.io.setting.HttpSetting.RecoveredHttpSetting
import refuel.http.io.setting.{HttpClientLogEnabled, HttpSetting}
import refuel.http.io.task.execution.HttpResultExecution
import refuel.injector.{AutoInject, Injector}
import refuel.json.JsonTransform
import refuel.provider.Lazy

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

@deprecated("Instead, use dependency injection")
@Inject[Finally]
object Http
    extends Http(new Lazy[HttpSetting] {
      override def _provide(implicit ctn: Container): HttpSetting = new RecoveredHttpSetting
    }) {}

/** {{{
  * class MyRepository(http: Http) extends AutoInject {
  *   import http._
  *
  *   http[GET]("http://???")
  *     .asString.run
  * }
  * }}}
  */
@Inject[Finally]
class Http(val setting: Lazy[HttpSetting]) extends Injector with JsonTransform with AutoInject with LazyLogging {

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
  def http[T <: HttpMethod.Method: MethodType](
      uri: Uri
  )(
      implicit logging: HttpClientLogEnabled = HttpClientLogEnabled.Default
  ): HttpRunner[HttpResponse] = {
    new HttpRunner[HttpResponse](
      setting.requestBuilder(
        HttpRequest(implicitly[MethodType[T]].method).withUri(uri)
      ),
      new HttpResultExecution[HttpResponse] {
        def execute(request: HttpRequest)(implicit as: ActorSystem): Future[HttpResponse] = {
          implicit val ec: ExecutionContext = as.dispatcher
          (
            if (logging.enabled) {
              request.entity.dataBytes
                .runReduce(_ ++ _)
                .map(_.utf8String)
                .map { req =>
                  logger.info(s"Try to request ${request.method.value}: ${request.uri.toString()} => $req")
                }
            } else Future.unit
          ).map(_ => System.currentTimeMillis())
            .flatMap {
              from =>
                HttpRetryRevolver(setting.retryThreshold)
                  .revolving() {
                    akka.http.scaladsl.Http().singleRequest(request)
                  }
                  .flatMap { res =>
                    if (logging.enabled) {
                      logger.info(
                        s"Http request completed. ${(System
                          .currentTimeMillis() - from).toFloat / 1000} sec [ ${request.method.value}: ${request.uri.toString()} ]"
                      )
                    }
                    if (res.status.isSuccess()) Future(res)
                    else Future.failed(HttpErrorRaw(res, None))
                  }
            }
        }
      }
    )
  }
}
