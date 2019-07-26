package com.phylage.scaladia.http.io

import akka.http.javadsl.model.ContentType.WithCharset
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest, HttpResponse, Uri}
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
      * @return
      */
    def asString: HttpRunner[String] = {
      value.flatMap(_.entity.dataBytes.runFold(ByteString.empty)(_ ++ _).map(_.utf8String))
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

private case class HttpRetryRevolver(maxRetry: Int) extends Injector {

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

sealed class HttpRunner[T](request: HttpRequest, task: HttpResultTask[T]) extends JacksonParser {

  import akka.http.scaladsl.model.HttpCharsets._
  import akka.http.scaladsl.model.MediaTypes._

  private[this] implicit val sys = Http.setting.actorSystem
  private[this] implicit val mat = Http.setting.actorMaterializer(sys)

  /**
    * Set a request body.
    *
    * @param value       Request body. It will be serialized by Jackson.
    * @param withCharset charset
    * @tparam X Request body type.
    * @return
    */
  def body[X](value: X, withCharset: WithCharset = `text/plain` withCharset `UTF-8`): HttpRunner[T] = {
    new HttpRunner[T](
      request.withEntity(withCharset, serialize(value)),
      task
    )
  }

  /**
    * Set a requets header.
    *
    * @param key   header key
    * @param value header value
    * @return
    */
  def header(key: String, value: String): HttpRunner[T] = new HttpRunner[T](
    request.withHeaders(RawHeader(key, value)),
    task
  )

  /**
    * Set a requets header.
    *
    * @param header header
    * @return
    */
  def header(header: HttpHeader): HttpRunner[T] = new HttpRunner[T](
    request.withHeaders(header),
    task
  )

  /**
    * To synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  def map[R](func: T => R): HttpRunner[R] = new HttpRunner[R](
    request,
    new HttpResultTask[R] {
      def execute(request: HttpRequest): Future[R] = run.map(func)
    }
  )

  /**
    * Execute future functions.
    *
    * @return
    */
  def run: Future[T] = task.execute(request)

  /**
    * To flatten synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  def flatMap[R](func: T => Future[R]): HttpRunner[R] = new HttpRunner[R](
    request,
    new HttpResultTask[R] {
      def execute(request: HttpRequest): Future[R] = run.flatMap(func)
    }
  )

  private[http] def entity = request
}

trait HttpResultTask[T] extends JacksonParser {
  def execute(request: HttpRequest): Future[T]
}

object HttpMethod {

  abstract class Method

  class CONNECT extends Method

  class HEAD extends Method

  class OPTIONS extends Method

  class PATCH extends Method

  class TRACE extends Method

  class GET extends Method

  class PUT extends Method

  class POST extends Method

  class DELETE extends Method

}