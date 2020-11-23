package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentType.NonBinary
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest, HttpResponse}
import com.typesafe.scalalogging.LazyLogging
import refuel.http.io.setting.HttpSetting
import refuel.http.io.task.execution.HttpResultExecution
import refuel.http.io.task.{CombineTask, HttpTask, StrictTask}
import refuel.json.JsonTransform
import refuel.json.codecs.definition.AnyRefCodecs
import refuel.json.codecs.{Read, Write}
import refuel.json.logging.JsonConvertLogEnabled

import scala.concurrent.Future

object HttpRunner {

  import scala.concurrent.duration._

  implicit class HttpResponseStream(value: HttpTask[HttpResponse])
      extends JsonTransform
      with AnyRefCodecs
      with LazyLogging {

    private[this] final def convert[R: Read](
        x: HttpResponse,
        recover: (HttpTask[R], String) => HttpTask[R] = (x: HttpTask[R], _: String) => x
    )(
        implicit setting: HttpSetting,
        timeout: FiniteDuration = 30.seconds,
        logEnabled: JsonConvertLogEnabled
    ): HttpTask[R] = {
      new StrictTask(x).flatMap { strict =>
        recover(
          map[String, R] { implicit as => res =>
            res.as[R].fold(e => Future.failed(HttpErrorRaw(x, Some(strict), e)), Future(_)(as.dispatcher))
          }(strict),
          strict
        )
      }
    }

    /** Sets the Unmarshaller of normal and error, and returns [[T]] on success or `HttpResponseError[E]` on failure.
      * This [[E]] is used when HttpStatus is not a success, or when HttpResponse is explicitly combined with HttpErrorRaw.
      * For example, if HttpStatus is 200 and you need to handle success and failure, you need to define and use the `Either[T, E]` Reader.
      *
      * If you want to handle the result of a specific json key, define your own Reader of
      * Either[L, R] and use a bipolar transformer such as `transform[Either[L, R], E]`.
      *
      * @return
      */
    final def transform[T: Read, E <: Throwable: Read](
        implicit setting: HttpSetting,
        timeout: FiniteDuration = 30.seconds,
        logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
    ): HttpTask[T] = {
      value
        .recoverWith {
          case HttpErrorRaw(res, _, _) =>
            new StrictTask(res).flatMap { strict =>
              map[String, HttpResponse] { implicit as => _res =>
                _res
                  .as[E]
                  .fold[Future[HttpResponse]](x => Future.failed(HttpErrorRaw(res, Some(strict), x)), Future.failed(_))
              }(strict)
            }
        }
        .flatMap(
          convert[T](
            _, {
              case (convertTask, strict) =>
                convertTask.recoverWith {
                  case HttpErrorRaw(res, _, _) =>
                    map[String, T] { implicit as => _res =>
                      _res
                        .as[E]
                        .fold[Future[T]](x => Future.failed(HttpErrorRaw(res, Some(strict), x)), Future.failed(_))
                    }(strict)
                }
            }
          )
        )
    }

    /** If the Request is successful, it is always handled by Read[R]. If the request fails, it is handled by Read[L].
      *
      * If you want to handle the result of a specific json key, define your own Reader of
      * Either[L, R] and use a bipolar transformer such as `transform[Either[L, R], E]`.
      */
    final def eitherMap[L: Read, R: Read](
        implicit setting: HttpSetting,
        timeout: FiniteDuration = 30.seconds,
        logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
    ): HttpTask[Either[L, R]] = {
      as[Either[L, R]]
    }

    /** If the Http request succeeds, it is handled by Read[R]; if Read[R] fails, it is handledAnyRefCodecs by Read[L];
      * if Read[L] also fails, it returns Future. HttpResponse))).
      * If the Http request fails, handle the response body with Read[E].
      *
      * If you want to handle the result of a specific json key, define your own Reader of
      * Either[L, R] and use a bipolar transformer such as `transform[Either[L, R], E]`.
      *
      * @param setting
      * @param timeout
      * @tparam L
      * @tparam R
      * @tparam E
      * @return
      */
    final def eitherTransform[L: Read, R: Read, E <: Throwable: Read](
        implicit setting: HttpSetting,
        timeout: FiniteDuration = 30.seconds,
        logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
    ): HttpTask[Either[L, R]] = {
      transform[Either[L, R], E](EitherCodec[L, R, Read], implicitly[Read[E]], setting, timeout)
    }

    /** Regist a type of returning deserialized json texts.
      * If you still need to unmarshall on errors, use transform instead.
      *
      * @tparam X Deserialized type.
      * @return
      */
    final def as[X: Read](
        implicit setting: HttpSetting,
        timeout: FiniteDuration = 30.seconds,
        logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
    ): HttpTask[X] = {
      value.flatMap(convert[X](_))
    }

    /** Regist a type of returning deserialized json texts.
      * There is a 3 second timeout to load all streams into memory.
      *
      * The current development progress does not support Streaming call.
      *
      * @return
      */
    final def asString(implicit setting: HttpSetting, timeout: FiniteDuration = 30.seconds): HttpTask[String] = {
      value.flatMap(new StrictTask(_))
    }
  }

}

class HttpRunner[T](request: HttpRequest, task: HttpResultExecution[T]) extends JsonTransform with HttpTask[T] {
  me =>

  import akka.http.scaladsl.model.MediaTypes._

  /**
    * Set a request body.
    *
    * @param value       Request body. It will be serialized by Jackson.
    * @param withCharset charset
    * @tparam X Request body type.
    * @return
    */
  def body[X: Write](value: X, withCharset: NonBinary = `application/json`): HttpRunner[T] = {
    new HttpRunner[T](
      request.withEntity(withCharset, value.toJString),
      task
    )
  }

  /**
    * Set a request body.
    * You can set up a standard request that is supported by Akka HTTP.
    *
    * @param f Request setting function.
    * @return
    */
  def mapRequest(f: HttpRequest => HttpRequest): HttpRunner[T] = {
    new HttpRunner[T](
      f(request),
      task
    )
  }

  /**
    * Set request parameters.
    *
    * @param ps parameters.
    * @return
    */
  def params(ps: (String, String)*): HttpRunner[T] = {
    new HttpRunner[T](
      request.withUri(request.uri.withQuery(Query(ps: _*))),
      task
    )
  }

  /**
    * Request builds supported by Akka http are available.
    *
    * @param fn request builder
    * @return
    */
  def requestMap(fn: HttpRequest => HttpRequest): HttpRunner[T] = {
    new HttpRunner[T](
      fn(request),
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
  def map[R](func: T => R): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.map(func)(as.dispatcher)
  }

  /**
    * To synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  def mapF[R](func: T => Future[R]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.flatMap(func)(as.dispatcher)
  }

  /**
    * Execute future functions.
    *
    * @return
    */
  def run(implicit as: ActorSystem): Future[T] = task.execute(request)

  /**
    * To flatten synthesize.
    *
    * @param func Synthesis processing.
    * @tparam R Synthesize return type.
    * @return
    */
  def flatMap[R](func: T => HttpTask[R]): HttpTask[R] = {
    new CombineTask[R] {
      override def run(implicit as: ActorSystem): Future[R] = me.run.flatMap(func(_).run)(as.dispatcher)
    }
  }

  override def recover[R >: T](f: PartialFunction[Throwable, R]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] =
      me.run.recoverWith[R](f.andThen(x => Future.apply(x)(as.dispatcher)))(as.dispatcher)
  }

  override def recoverWith[R >: T](f: PartialFunction[Throwable, HttpTask[R]]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.recoverWith[R](f.andThen(_.run))(as.dispatcher)
  }

  override def recoverF[R >: T](f: PartialFunction[Throwable, Future[R]]): HttpTask[R] = new CombineTask[R] {
    override def run(implicit as: ActorSystem): Future[R] = me.run.recoverWith[R](f)(as.dispatcher)
  }
}
