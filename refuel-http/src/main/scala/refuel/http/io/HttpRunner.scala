package refuel.http.io

import akka.http.scaladsl.model.ContentType.NonBinary
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed class HttpRunner[T](request: HttpRequest, task: HttpResultTask[T]) extends JacksonParser {

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
  def body[X](value: X, withCharset: NonBinary = `application/json`): HttpRunner[T] = {
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
