package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.util.ByteString
import refuel.http.io.setting.HttpSetting
import refuel.http.io.setting.HttpSetting.RecoveredHttpSetting
import refuel.http.io.task.HttpTask
import refuel.http.io.task.execution.HttpResultExecution
import refuel.injector.Injector
import refuel.json.JsonTransform
import refuel.json.codecs.Read

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.implicitConversions

object Http extends HttpClient(new RecoveredHttpSetting)

class HttpClient(val setting: HttpSetting) extends Injector with JsonTransform {

  implicit def toUri(uri: String): Uri = Uri(uri)

  private lazy final val URL_PARAM_FORMAT = "%s=%s"

  implicit class UrlParameters(value: Map[String, Any]) {

    /**
      * Convert to get request string.
      *
      * @return "x=y&a=b&1=2"
      */
    def asUrl: String = {
      value.toSeq.map { x => URL_PARAM_FORMAT.format(x._1, x._2.toString) }.mkString("&")
    }
  }

  implicit class HttpResponseStream(value: HttpRunner[HttpResponse]) {

    /**
      * Regist a type of returning deserialized json texts.
      *
      * @tparam X Deserialized type.
      * @return
      */
    def as[X: Read]: HttpTask[X] = {
      asString
        .flatMap({ implicit as => res => res.as[X].fold(Future.failed, Future(_)(as.dispatcher)) }: ActorSystem => String => Future[
            X
          ]
        )
    }

    /**
      * Regist a type of returning deserialized json texts.
      * There is a 3 second timeout to load all streams into memory.
      *
      * The current development progress does not support Streaming call.
      *
      * @return
      */
    def asString: HttpTask[String] = {
      value.flatMap({ implicit as => res =>
        res.entity
          .toStrict(3.seconds)
          .flatMap(
            setting.responseBuilder(_).dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String)(as.dispatcher)
          )(as.dispatcher)
      }: ActorSystem => HttpResponse => Future[String])
    }
  }

  /**
    * Create a http request task.
    * {{{
    *   import refuel.http.io.Http._
    *
    *   val requets = Map(
    *     "id" -> 1,
    *     "name" -> "Jack"
    *   )
    *
    *   val result: FutureSearch.Response =
    *     http[GET](s"http://localhost:80/?${requets.asUrl}".withQuery(Map("param" -> "value")))
    *     .header("auth", "abcde")
    *     .deserializing[ResponseType]
    *     .map(_.value)
    *     .flatMap(FutureSearch.byValue)
    *     .run
    * }}}
    *
    * @param uri Request url
    * @tparam T Request method type. See [[refuel.http.io.HttpMethod]]
    * @return
    */
  def http[T <: HttpMethod.Method: MethodType](uri: Uri): HttpRunner[HttpResponse] = {
    new HttpRunner[HttpResponse](
      setting.requestBuilder(
        HttpRequest(implicitly[MethodType[T]].method).withUri(uri)
      ),
      new HttpResultExecution[HttpResponse] {
        def execute(request: HttpRequest)(implicit as: ActorSystem): Future[HttpResponse] =
          HttpRetryRevolver(setting.retryThreshold).revolving() {
            akka.http.scaladsl.Http().singleRequest(request)
          }
      }
    )
  }
}
