package refuel.http

import java.util.Optional

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.testkit.ScalatestRouteTest
import refuel.AkkaHttpWebContext
import refuel.injector.Injector
import refuel.saml.SAMLAuthConfig
import refuel.session.SessionIDGenerator
import refuel.store.{ForgetfulSessionStorage, InMemorySessionStorage}

class AkkaHttpSessionStoreTest
    extends org.scalatest.wordspec.AnyWordSpecLike
    with org.scalatest.matchers.should.Matchers
    with ScalatestRouteTest
    with Injector {
  "AkkaHttpSessionStore.get" should {
    "return null when the data is not available" in {
      new AkkaHttpSessionStore().get(
        new AkkaHttpWebContext(
          HttpRequest(),
          Map.empty,
          new ForgetfulSessionStorage()
        )(inject[SAMLAuthConfig], new SessionIDGenerator()),
        "mykey"
      ) shouldBe Optional.empty()
    }

    "return the data when available" in {
      val context = new AkkaHttpWebContext(
        HttpRequest(),
        Map.empty,
        inject[InMemorySessionStorage]
      )(inject[SAMLAuthConfig], new SessionIDGenerator())
      new AkkaHttpSessionStore().set(context, "mykey", "yooo")
      new AkkaHttpSessionStore().get(context, "mykey") shouldBe Optional.of("yooo")
    }
  }
}
