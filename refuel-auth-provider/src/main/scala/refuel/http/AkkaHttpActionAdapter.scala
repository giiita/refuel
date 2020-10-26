package refuel.http

import akka.http.scaladsl.model.StatusCodes.{custom, OK, SeeOther, Unauthorized}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.server.RouteResult.Complete
import com.typesafe.scalalogging.LazyLogging
import org.pac4j.core.exception.http.{FoundAction, HttpAction, OkAction, SeeOtherAction, UnauthorizedAction}
import org.pac4j.core.http.adapter.HttpActionAdapter
import refuel.AkkaHttpWebContext

import scala.concurrent.Future

object AkkaHttpActionAdapter extends HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext] with LazyLogging {
  override def adapt(action: HttpAction, context: AkkaHttpWebContext): Future[Complete] = {
    Future.successful(Complete {
      logger.debug(s"Security Action completed. ${action.getClass.getName}(${action.getCode}) ${action.getMessage}")
      context.addResponseSessionCookie()
      action match {
        case _: UnauthorizedAction =>
          // XHR requests don't receive a TEMP_REDIRECT but a UNAUTHORIZED. The client can handle this
          // to trigger the proper redirect anyway, but for a correct flow the session cookie must be set
          HttpResponse(Unauthorized)
        case a: FoundAction =>
          HttpResponse(SeeOther, headers = List[HttpHeader](Location(Uri(a.getLocation))))
        case a: SeeOtherAction =>
          HttpResponse(SeeOther, headers = List[HttpHeader](Location(Uri(a.getLocation))))
        case a: OkAction =>
          val contentBytes = a.getContent.getBytes
          val entity =
            context.getContentType.map(ct => HttpEntity(ct, contentBytes)).getOrElse(HttpEntity(contentBytes))
          HttpResponse(OK, entity = entity)
        case _ =>
          HttpResponse(StatusCodes.getForKey(action.getCode).getOrElse(custom(action.getCode, action.getMessage)))
      }
    })
  }
}
