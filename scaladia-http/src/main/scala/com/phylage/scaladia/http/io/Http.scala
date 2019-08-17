package com.phylage.scaladia.http.io

import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.util.ByteString
import com.phylage.scaladia.http.io.setting.HttpSetting
import com.phylage.scaladia.injector.Injector
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.ClassTag

object Http extends Injector {
  private lazy final val URL_PARAM_FORMAT = "%s=%s"
  private val logger: Logger = Logger("Http")

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
    def asLimitCut[X: ClassTag]: HttpRunner[X] = {
      asStringLimitCut.map(super.deserialize[X])
    }

    /**
      * Regist a type of returning deserialized json texts.
      *
      * @return
      */
    def asString: HttpRunner[String] = {
      value.flatMap(_.entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String))
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
    def asStringLimitCut: HttpRunner[String] = {
      value.flatMap(_.entity.withoutSizeLimit().dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String))
    }
  }

  /**
    * Create a http request task.
    * {{{
    *   import com.github.giiita.io.http.Http._
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
    logger.info(s"Setup http request [ $urlString ]")

    new HttpRunner[HttpResponse](
      HttpRequest(implicitly[MethodType[T]].method).withUri(Uri(urlString)),
      new HttpResultTask[HttpResponse] {
        def execute(request: HttpRequest): Future[HttpResponse] = HttpRetryRevolver(setting.retryThreshold).revolving() {
          akka.http.scaladsl.Http()(setting.actorSystem).singleRequest(request)
        }
      }
    )
  }
}



