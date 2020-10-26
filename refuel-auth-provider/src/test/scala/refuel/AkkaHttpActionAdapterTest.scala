package refuel

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, HttpResponse}
import akka.util.ByteString
import org.pac4j.core.exception.http.{
  BadRequestAction,
  ForbiddenAction,
  HttpAction,
  NoContentAction,
  OkAction,
  SeeOtherAction,
  StatusAction,
  UnauthorizedAction
}
import org.scalatest.concurrent.ScalaFutures
import refuel.http.AkkaHttpActionAdapter
import refuel.injector.Injector
import refuel.saml.SAMLAuthConfig
import refuel.session.SessionIDGenerator
import refuel.store.ForgetfulSessionStorage

class AkkaHttpActionAdapterTest
    extends org.scalatest.wordspec.AnyWordSpecLike
    with org.scalatest.matchers.should.Matchers
    with ScalaFutures
    with Injector {
  implicit val as: ActorSystem = ActorSystem()
  "AkkaHttpActionAdapter" should {
    "convert 200 to OK" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(200), context).futureValue.response shouldEqual HttpResponse(
        OK,
        Nil,
        HttpEntity(ContentTypes.`application/octet-stream`, ByteString(""))
      )
    }
    "convert 401 to Unauthorized" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(401), context).futureValue.response shouldEqual HttpResponse(
        Unauthorized
      )
    }
    "convert 302 to SeeOther (to support login flow)" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(302), context).futureValue.response shouldEqual HttpResponse(
        SeeOther
      )
    }
    "convert 400 to BadRequest" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(400), context).futureValue.response shouldEqual HttpResponse(
        BadRequest
      )
    }
    "convert 201 to Created" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(200), context).futureValue.response shouldEqual HttpResponse(Created)
    }
    "convert 403 to Forbidden" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(403), context).futureValue.response shouldEqual HttpResponse(
        Forbidden
      )
    }
    "convert 204 to NoContent" in withContext { context =>
      AkkaHttpActionAdapter.adapt(new StatusAction(204), context).futureValue.response shouldEqual HttpResponse(
        NoContent
      )
    }
    "convert 200 to OK with content type set from the context" in withContext { context =>
      context.setResponseContentType("application/json")
      AkkaHttpActionAdapter.adapt(new StatusAction(200), context).futureValue.response shouldEqual HttpResponse.apply(
        OK,
        Nil,
        HttpEntity(ContentTypes.`application/json`, ByteString(""))
      )
    }
  }

  def withContext(f: AkkaHttpWebContext => Unit)(implicit as: ActorSystem) = {
    new AkkaHttpWebContext(
      HttpRequest(),
      Map.empty,
      new ForgetfulSessionStorage()
    )(inject[SAMLAuthConfig], new SessionIDGenerator())
  }
}
