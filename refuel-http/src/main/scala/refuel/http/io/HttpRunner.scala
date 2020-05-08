package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentType.NonBinary
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest}
import refuel.json.JsonTransform
import refuel.json.codecs.Write

import scala.concurrent.Future

sealed class HttpRunner[T](request: HttpRequest, task: HttpResultTask[T]) extends JsonTransform {

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
  def map[R](func: T => R): HttpRunner[R] = new HttpRunner[R](
    request,
    new HttpResultTask[R] {
      def execute(request: HttpRequest)(implicit as: ActorSystem): Future[R] = run.map(func)(as.dispatcher)
    }
  )

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
  def flatMap[R](func: T => Future[R]): HttpRunner[R] = new HttpRunner[R](
    request,
    new HttpResultTask[R] {
      def execute(request: HttpRequest)(implicit as: ActorSystem): Future[R] = run.flatMap(func)(as.dispatcher)
    }
  )

  private[http] def entity = request

  def flatMapAs[R](func: ActorSystem => T => Future[R]) = new HttpRunner[R](
    request,
    new HttpResultTask[R] {
      def execute(request: HttpRequest)(implicit as: ActorSystem): Future[R] = run.flatMap(func(as))(as.dispatcher)
    }
  )
}
