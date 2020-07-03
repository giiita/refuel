package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentType.NonBinary
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpHeader, HttpRequest}
import refuel.http.io.task.execution.HttpResultExecution
import refuel.http.io.task.{CombineTask, HttpTask}
import refuel.json.JsonTransform
import refuel.json.codecs.Write

import scala.concurrent.Future

sealed class HttpRunner[T](request: HttpRequest, task: HttpResultExecution[T]) extends JsonTransform with HttpTask[T] {
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
