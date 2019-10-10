package com.phylage.scaladia.http.io

import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.util.ByteString
import com.phylage.scaladia.http.io.setting.HttpSetting
import com.phylage.scaladia.injector.Injector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.reflect.ClassTag

object Http extends Injector {
  private[this] lazy final val URL_PARAM_FORMAT = "%s=%s"

  private[http] val setting = inject[HttpSetting]

  implicit class UrlParameters(value: Map[String, Any]) {
    /**
     * Convert to get request string.
     *
     * @return "x=y&a=b&1=2"
     */
    def asUrl: String = {
      value.toSeq.map { x =>
        URL_PARAM_FORMAT.format(x._1, x._2.toString)
      }.mkString("&")
    }
  }

  implicit class HttpResponseStream(value: HttpRunner[HttpResponse]) extends JacksonParser {

    private[this] implicit val sys = setting.actorSystem
    private[this] implicit val mat = setting.actorMaterializer(sys)

    /**
     * Regist a type of returning deserialized json texts.
     *
     * @tparam X Deserialized type.
     * @return
     */
    def as[X: ClassTag]: HttpRunner[X] = {
      asString.map(super.deserialize[X])
    }

    /**
     * Regist a type of returning deserialized json texts.
     *
     * Sets the upper limit for akka stream to close the stream.
     * [[akka.http.scaladsl.model.EntityStreamSizeException]] occurs when receiving a response exceeding the setting.
     * The usual limit is 8MB.
     *
     * @tparam X Deserialized type.
     * @return
     */
    @deprecated("Custom HttpSetting.responseBuilder instead. " +
      "class CustomHttpSetting() extends HttpSetting(responseBuilder = _.withoutSizeLimit()) with AutoInject[HttpSetting]")
    def asLimit[X: ClassTag](limit: Long): HttpRunner[X] = {
      asStringLimit(limit).map(super.deserialize[X])
    }

    /**
     * Regist a type of returning deserialized json texts.
     * Cut the reception size limit.
     * The usual limit is 8MB.
     *
     * @tparam X
     * @return
     */
    @deprecated("Custom HttpSetting.responseBuilder instead. " +
      "class CustomHttpSetting() extends HttpSetting(responseBuilder = _.withoutSizeLimit()) with AutoInject[HttpSetting]")
    def asLimitCut[X: ClassTag]: HttpRunner[X] = {
      asStringLimitCut.map(super.deserialize[X])
    }

    /**
     * Regist a type of returning deserialized json texts.
     * There is a 3 second timeout to load all streams into memory.
     *
     * The current development progress does not support Streaming call.
     * @return
     */
    def asString: HttpRunner[String] = {
      value.flatMap(_.entity.toStrict(3.seconds))
        .flatMap(setting.responseBuilder(_).dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String))
    }

    /**
     * Regist a type of returning deserialized json texts.
     *
     * Sets the upper limit for akka stream to close the stream.
     * [[akka.http.scaladsl.model.EntityStreamSizeException]] occurs when receiving a response exceeding the setting.
     * The usual limit is 8MB.
     *
     * @return
     */
    @deprecated("Custom HttpSetting.responseBuilder instead. " +
      "class CustomHttpSetting() extends HttpSetting(responseBuilder = _.withoutSizeLimit()) with AutoInject[HttpSetting]")
    def asStringLimit(limit: Long): HttpRunner[String] = {
      value.flatMap(_.entity.withSizeLimit(limit).dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String))
    }

    /**
     * Regist a type of returning deserialized json texts.
     * Cut the reception size limit.
     * The usual limit is 8MB.
     *
     * @return
     */
    @deprecated("Custom HttpSetting.responseBuilder instead. " +
      "class CustomHttpSetting() extends HttpSetting(responseBuilder = _.withoutSizeLimit()) with AutoInject[HttpSetting]")
    def asStringLimitCut: HttpRunner[String] = {
      value.flatMap(_.entity.withoutSizeLimit().dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String))
    }
  }

  /**
   * Create a http request task.
   * {{{
   *   import com.phylage.scaladia.http.io.Http._
   *
   *   val requets = Map(
   *     "id" -> 1,
   *     "name" -> "Jack"
   *   )
   *
   *   val result: FutureSearch.Response =
   *     http[GET](s"http://localhost:80/?${requets.asUrl}")
   *     .header("auth", "abcde")
   *     .deserializing[ResponseType]
   *     .map(_.value)
   *     .flatMap(FutureSearch.byValue)
   *     .run
   * }}}
   *
   * @param urlString Request url
   * @tparam T Request method type. See [[com.phylage.scaladia.http.io.HttpMethod]]
   * @return
   */
  def http[T <: HttpMethod.Method : MethodType](urlString: String): HttpRunner[HttpResponse] = {
    println(s"Setup http request [ $urlString ]")

    new HttpRunner[HttpResponse](
      setting.requestBuilder(
        HttpRequest(implicitly[MethodType[T]].method).withUri(Uri(urlString))
      ),
      new HttpResultTask[HttpResponse] {
        def execute(request: HttpRequest): Future[HttpResponse] = HttpRetryRevolver(setting.retryThreshold).revolving() {
          akka.http.scaladsl.Http()(setting.actorSystem).singleRequest(request)
        }
      }
    )
  }
}



