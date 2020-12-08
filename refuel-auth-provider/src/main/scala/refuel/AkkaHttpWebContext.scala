package refuel

import java.util.{Optional, UUID}

import akka.http.scaladsl.model.HttpHeader.ParsingResult.{Error, Ok}
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.model.{ContentType, HttpHeader, HttpRequest}
import org.pac4j.core.context.{Cookie, WebContext}
import refuel.authorizer.CsrfCookieAuthorizer
import refuel.http.AkkaHttpSessionStore
import refuel.saml.SAMLAuthConfig
import refuel.session.SessionIDGenerator
import refuel.store.SessionStorage

import scala.collection.JavaConverters._
import scala.compat.java8.OptionConverters._

/**
  *
  * The AkkaHttpWebContext is responsible for wrapping an HTTP request and stores changes that are produced by pac4j and
  * need to be applied to an HTTP response.
  */
case class AkkaHttpWebContext(
    request: HttpRequest,
    formFields: Map[String, String],
    private[refuel] val sessionStorage: SessionStorage
)(implicit conf: SAMLAuthConfig, gen: SessionIDGenerator)
    extends WebContext {
  import AkkaHttpWebContext._

  // Only compute the request cookies once
  private[this] lazy final val RequestCookies =
    request.cookies.map { akkaCookie => new Cookie(akkaCookie.name, akkaCookie.value) }.asJavaCollection
  override lazy val getSessionStore = new AkkaHttpSessionStore()
  @volatile private var changes     = ResponseChanges.empty
  @volatile var sessionId: SessionId = {
    request.cookies
      .collectFirst {
        case x if x.name == conf.sessionCookieName /* && sessionStorage.sessionExists(SessionId(x.value)) */ =>
          SessionId(x.value)
      }
      .getOrElse {
        val sessionId = gen.gen
        sessionStorage.createOrReuseSession(sessionId)
        sessionId
      }
  }

  //Request parameters are composed of form fields and the query part of the uri. Stored in a lazy val in order to only compute it once
  private[this] final lazy val requestParameters = formFields ++ request.getUri().query().toMap.asScala

  override def getRequestCookies: java.util.Collection[Cookie] = RequestCookies

  override def getRemoteAddr: String = {
    request.getUri().getHost.address()
  }

  override def setResponseHeader(name: String, value: String): Unit = {
    if (name.toLowerCase != "cache-control" && name.toLowerCase != "pragma") {
      val header = HttpHeader.parse(name, value) match {
        case Ok(h, _)     => h
        case Error(error) => throw new IllegalArgumentException(s"Error parsing http header ${error.formatPretty}")
      }

      // Avoid adding duplicate headers, Pac4J expects to overwrite headers like `Location`
      changes = changes.copy(headers = header :: changes.headers.filter(_.name != name))
    }
  }

  override def getRequestParameters: java.util.Map[String, Array[String]] = {
    requestParameters.map {
      case (k, v) => k -> Array(v)
    }.asJava
  }

  override def getFullRequestURL: String = {
    request.uri.withScheme("https").toString
  }

  override def getServerName: String = {
    request.getUri().host.address().split(":").head
  }

  override def setResponseContentType(contentType: String): Unit = {
    ContentType.parse(contentType) match {
      case Right(ct) =>
        changes = changes.copy(contentType = Some(ct))
      case Left(_) =>
        throw new IllegalArgumentException("Invalid response content type " + contentType)
    }
  }

  override def getPath: String = {
    request.getUri().path
  }

  override def getRequestParameter(name: String): Optional[String] = {
    requestParameters.get(name).asJava
  }

  override def getRequestHeader(name: String): Optional[String] = {
    request.headers.find(_.name().toLowerCase() == name.toLowerCase).map(_.value).asJava
  }

  override def getScheme: String = {
    request.getUri().getScheme
  }

  override def getRequestMethod: String = {
    request.method.value
  }

  override def getServerPort: Int = {
    request.getUri().getPort
  }

  override def setRequestAttribute(name: String, value: scala.AnyRef): Unit = {
    changes = changes.copy(attributes = changes.attributes ++ Map[String, AnyRef](name -> value))
  }

  override def getRequestAttribute(name: String): Optional[AnyRef] = {
    changes.attributes.get(name).asJava
  }

  def getContentType: Option[ContentType] = {
    changes.contentType
  }

  def getChanges: ResponseChanges = {
    changes
  }

  def addResponseSessionCookie(): Unit = {
    val cookie = new Cookie(conf.sessionCookieName, sessionId.value)
    cookie.setSecure(isSecure)
    cookie.setMaxAge(conf.lifetimeSeconds.toInt)
    conf.cookieDomain.foreach(cookie.setDomain)
    cookie.setHttpOnly(true)
    cookie.setPath(conf.cookiePath)
    addResponseCookie(cookie)
  }

  override def addResponseCookie(cookie: Cookie): Unit = {
    changes = changes.copy(cookies = changes.cookies :+ toAkkaHttpCookie(cookie))
  }

  private[this] final def toAkkaHttpCookie(cookie: Cookie): HttpCookie = {
    HttpCookie(
      name = cookie.getName,
      value = cookie.getValue,
      expires = None,
      maxAge = if (cookie.getMaxAge < 0) None else Some(cookie.getMaxAge),
      domain = Option(cookie.getDomain),
      path = Option(cookie.getPath),
      secure = cookie.isSecure,
      httpOnly = cookie.isHttpOnly,
      extension = conf.cookieExtension
    )
  }

  override def isSecure: Boolean = {
    conf.cookieSecure.getOrElse(request.getUri().getScheme == "https")
  }

  def addResponseCsrfCookie(): AkkaHttpWebContext = CsrfCookieAuthorizer.setup(this)

  private[refuel] def destroySession(): Boolean = {
    sessionStorage.destroySession(sessionId)
    sessionId = newSession()
    true
  }

  private[refuel] def trackSession(_sessionId: SessionId): Boolean = {
    sessionStorage.createOrReuseSession(_sessionId)
    sessionId = _sessionId
    true
  }

  private[this] def newSession(): SessionId = {
    val sessionId = SessionId(UUID.randomUUID().toString)
    sessionStorage.createOrReuseSession(sessionId)
    sessionId
  }
}

object AkkaHttpWebContext {

  case class SessionId(value: String) extends AnyVal

  /** This class is where all the HTTP response changes are stored so that they can later be applied to an HTTP Request */
  case class ResponseChanges private (
      headers: List[HttpHeader],
      contentType: Option[ContentType],
      content: String,
      cookies: List[HttpCookie],
      attributes: Map[String, AnyRef]
  )

  object ResponseChanges {
    def empty: ResponseChanges = {
      ResponseChanges(List.empty, None, "", List.empty, Map.empty)
    }
  }
}
