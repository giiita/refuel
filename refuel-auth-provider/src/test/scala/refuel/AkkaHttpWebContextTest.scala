package refuel

import java.util.Optional

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Cookie, HttpCookie}
import refuel.injector.Injector
import AkkaHttpWebContext.SessionId
import refuel.saml.SAMLAuthConfig
import refuel.session.SessionIDGenerator
import refuel.store.{ForgetfulSessionStorage, SessionStorage}

import scala.collection.JavaConverters._

class AkkaHttpWebContextTest
    extends org.scalatest.wordspec.AnyWordSpecLike
    with org.scalatest.matchers.should.Matchers
    with Injector {
  private[this] lazy final val cookie = ("cookieName", "cookieValue")

  "AkkaHttpWebContext" should {
    "get/set request cookies" in withContext(cookies = List(Cookie("cookieName", "cookieValue"))) { webContext =>
      val contextCookie = webContext.getRequestCookies
      contextCookie.asScala.map(cookie => (cookie.getName, cookie.getValue)) shouldEqual Seq(cookie)
    }

    "get a request header or an empty string when no such header exists" in withContext(requestHeaders =
      List(("foo", "bar"))
    ) { webContext =>
      webContext.getRequestHeader("foo") shouldEqual Optional.of("bar")
      webContext.getRequestHeader("abc") shouldEqual Optional.empty
    }
    "make sure request headers are case insensitive" in withContext(requestHeaders = List(("foo", "bar"))) {
      webContext => webContext.getRequestHeader("FOO") shouldEqual Optional.of("bar")
    }

    "get the full url including port from a request" in withContext(
      url = "/views/#/something.html",
      hostAddress = "localhost",
      hostPort = 7070
    ) { webContext => webContext.getFullRequestURL shouldEqual "http://localhost:7070/views/#/something.html" }

    "get the full url excluding port from a request" in withContext(url = "/views", hostAddress = "localhost") {
      webContext => webContext.getFullRequestURL shouldEqual "http://localhost/views"
    }

    "get the full url including query" in withContext(url = "/views.html?bla=bla&bla=bla", hostAddress = "localhost") {
      webContext => webContext.getFullRequestURL shouldEqual "http://localhost/views.html?bla=bla&bla=bla"
    }

    "get the request path" in withContext(url = "www.stackstate.com/views") { webContext =>
      webContext.getPath shouldEqual "www.stackstate.com/views"
    }

    "get the remote address from a request" in withContext(url = "/views", hostAddress = "192.1.1.1", hostPort = 8000) {
      webContext => webContext.getRemoteAddr shouldEqual "192.1.1.1"
    }

    "get the request http method" in withContext() { webContext => webContext.getRequestMethod shouldEqual "GET" }

    "get server name and port from the request" in withContext(hostAddress = "192.1.1.1", hostPort = 8000) {
      webContext =>
        webContext.getServerName shouldEqual "192.1.1.1"
        webContext.getServerPort shouldEqual 8000
    }

    "get the uri scheme" in withContext() { webContext => webContext.getScheme shouldEqual "http" }

    "get the request parameters or an empty string when no such parameter exists" in withContext(url =
      "www.stackstate.com/views?v=1234"
    ) { webContext =>
      webContext.getRequestParameter("v") shouldEqual Optional.of("1234")
      webContext.getRequestParameter("p") shouldEqual Optional.empty
    }

    "get/set request attributes" in withContext() { webContext =>
      webContext.setRequestAttribute("foo", "bar")
      webContext.getRequestAttribute("foo") shouldEqual Optional.of("bar")
      webContext.getRequestAttribute("foozor") shouldEqual Optional.empty
    }

    "get/set content type" in withContext() { webContext =>
      webContext.setResponseContentType("application/json")
      webContext.getContentType shouldEqual Some(ContentTypes.`application/json`)
    }

    "know if a url is secure" in withContext(scheme = "https") { webContext => webContext.isSecure shouldEqual true }

    "return form fields in the request parameters" in withContext(formFields = Map(("username", "testuser"))) {
      webContext =>
        webContext.getRequestParameters.containsKey("username") shouldEqual true
        webContext.getRequestParameter("username") shouldEqual Optional.of("testuser")
    }

    "add a cookie when the session is persisted and put in the expected data" in withContext(sessionStorage =
      new ForgetfulSessionStorage {
        override def renewSession(session: SessionId): Boolean = true
      }
    ) { webContext =>
      val conf = inject[SAMLAuthConfig]
      webContext.addResponseSessionCookie()
      webContext.getChanges.cookies.find(_.name == conf.sessionCookieName) shouldBe Some(
        HttpCookie(
          name = conf.sessionCookieName,
          value = webContext.sessionId.value,
          expires = None,
          maxAge = Some(0),
          domain = None,
          path = Some("/"),
          secure = false,
          httpOnly = true,
          extension = None
        )
      )
    }

    "don't add a cookie when the session was expired" in withContext(sessionStorage = new ForgetfulSessionStorage {
      override def renewSession(session: SessionId): Boolean = false
    }) { webContext =>
      val conf = inject[SAMLAuthConfig]
      webContext.getChanges.cookies.find(_.name == conf.sessionCookieName) shouldBe None
    }

    "make the session cookie secure when running over https" in withContext(
      scheme = "https",
      sessionStorage = new ForgetfulSessionStorage {
        override def renewSession(session: SessionId): Boolean = true
      }
    ) { webContext =>
      val conf = inject[SAMLAuthConfig]
      webContext.addResponseSessionCookie()
      webContext.getChanges.cookies.find(_.name == conf.sessionCookieName).get.secure shouldBe true
    }

    "pick up the session cookie and send it back" in {
      val conf = inject[SAMLAuthConfig]
      withContext(
        cookies = List(Cookie(conf.sessionCookieName, "my_session")),
        sessionStorage = new ForgetfulSessionStorage {
          override def sessionExists(key: SessionId): Boolean    = true
          override def renewSession(session: SessionId): Boolean = true
        }
      ) { webContext =>
        webContext.addResponseSessionCookie()
        webContext.getChanges.cookies.exists(_.name == conf.sessionCookieName) shouldEqual true
      }
    }

    "pick up the session cookie from cookies that are no longer sessions" in {
      val conf = inject[SAMLAuthConfig]
      withContext(
        cookies = List(
          Cookie(conf.sessionCookieName, "some_session"),
          Cookie(conf.sessionCookieName, "my_session")
        ),
        sessionStorage = new ForgetfulSessionStorage {
          override def sessionExists(key: SessionId): Boolean    = key.value == "my_session"
          override def renewSession(session: SessionId): Boolean = true
        }
      ) { webContext =>
        webContext.addResponseSessionCookie()
        webContext.getChanges.cookies
          .find(_.name == conf.sessionCookieName)
          .get
          .value shouldEqual "my_session"
      }
    }

    "creates a new sessionId when the cookie was expired" in {
      val conf = inject[SAMLAuthConfig]
      withContext(
        cookies = List(Cookie(conf.sessionCookieName, "my_session")),
        sessionStorage = new ForgetfulSessionStorage {
          override def sessionExists(key: SessionId): Boolean    = false
          override def renewSession(session: SessionId): Boolean = true
        }
      ) { webContext => webContext.sessionId shouldNot equal("my_session") }
    }

    "creates a new sessionId when the session was destroyed" in {
      val conf = inject[SAMLAuthConfig]
      withContext(
        cookies = List(Cookie(conf.sessionCookieName, "my_session")),
        sessionStorage = new ForgetfulSessionStorage {
          override def renewSession(session: SessionId): Boolean = true
        }
      ) { webContext =>
        webContext.destroySession()
        webContext.sessionId shouldNot equal("my_session")
      }
    }

    "stores the trackable session when requested" in {
      val conf = inject[SAMLAuthConfig]
      withContext(
        cookies = List(Cookie(conf.sessionCookieName, "my_session")),
        sessionStorage = new ForgetfulSessionStorage {
          override def renewSession(session: SessionId): Boolean = true
        }
      ) { webContext =>
        webContext.trackSession(SessionId("my_session2"))
        webContext.sessionId shouldBe SessionId("my_session2")
      }
    }
  }

  def withContext(
      requestHeaders: List[(String, String)] = List.empty,
      cookies: List[Cookie] = List.empty,
      url: String = "",
      scheme: String = "http",
      hostAddress: String = "",
      hostPort: Int = 0,
      formFields: Map[String, String] = Map.empty,
      sessionStorage: SessionStorage = new ForgetfulSessionStorage
  )(f: AkkaHttpWebContext => Unit): Unit = {
    val parsedHeaders: List[HttpHeader] =
      requestHeaders.map { case (k, v) => HttpHeader.parse(k, v) }.collect { case Ok(header, _) => header }
    val completeHeaders: List[HttpHeader] = parsedHeaders ++ cookies
    val uri                               = Uri(url).withScheme(scheme).withAuthority(hostAddress, hostPort)
    val request                           = HttpRequest(uri = uri, headers = completeHeaders)

    f(AkkaHttpWebContext(request, formFields, sessionStorage)(inject[SAMLAuthConfig], inject[SessionIDGenerator]))
  }
}
