package refuel

import java.util

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Route, RouteResult}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.RouteResult.Complete
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.pac4j.core.authorization.authorizer.Authorizer
import org.pac4j.core.client.{Clients, IndirectClient}
import org.pac4j.core.config.Config
import org.pac4j.core.context.{Cookie, WebContext}
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.engine.{
  DefaultCallbackLogic,
  DefaultLogoutLogic,
  DefaultSecurityLogic,
  SecurityGrantedAccessAdapter
}
import org.pac4j.core.exception.http.HttpAction
import org.pac4j.core.http.adapter.HttpActionAdapter
import org.pac4j.core.profile.{CommonProfile, UserProfile}
import org.pac4j.core.util.Pac4jConstants
import refuel.http.AkkaHttpActionAdapter
import refuel.injector.Injector
import refuel.lang.ScalaTime
import AkkaHttpSecurity.{AkkaHttpCallbackLogic, AkkaHttpLogoutLogic, AkkaHttpSecurityLogic}
import refuel.saml.{AuthnSAMLBuilder, SAMLAuthConfig}
import refuel.session.SessionIDGenerator
import refuel.store.{ForgetfulSessionStorage, InMemorySessionStorage, SessionStorage}

import scala.collection.JavaConverters._
import scala.concurrent.Future

class AkkaHttpSecurityTest
    extends org.scalatest.wordspec.AnyWordSpecLike
    with org.scalatest.matchers.should.Matchers
    with ScalatestRouteTest
    with Injector {

  implicit val as: ActorSystem = ActorSystem()
  val conf                     = inject[AuthnSAMLBuilder]

  "AkkaHttpSecurity" should {
    "set the proper defaults" in {
      shade { implicit c =>
        new ForgetfulSessionStorage().index()
        val akkaHttpSecurity = inject[AuthnSAMLBuilder].Default

        akkaHttpSecurity.ActionAdapter shouldBe AkkaHttpActionAdapter
        akkaHttpSecurity.SecurityLogic.getClass shouldBe classOf[DefaultSecurityLogic[_, _]]
        akkaHttpSecurity.CallbackLogic.getClass shouldBe classOf[DefaultCallbackLogic[_, _]]
      }
    }
  }

  "AkkaHttpSecurity.withAuthentication" should {
    "uses provided securityLogic and pass the expected parameters" in {
      shade { implicit c =>
        new ForgetfulSessionStorage().index()
        val config = new Config()

        val actionAdapter = new HttpActionAdapter[HttpResponse, AkkaHttpWebContext] {
          override def adapt(action: HttpAction, context: AkkaHttpWebContext): HttpResponse = ???
        }

        config.setHttpActionAdapter(actionAdapter)
        config.setSecurityLogic(new AkkaHttpSecurityLogic {
          override def perform(
              context: AkkaHttpWebContext,
              config: Config,
              securityGrantedAccessAdapter: SecurityGrantedAccessAdapter[Future[RouteResult], AkkaHttpWebContext],
              httpActionAdapter: HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext],
              clients: String,
              authorizers: String,
              matchers: String,
              multiProfile: java.lang.Boolean,
              parameters: AnyRef*
          ): Future[RouteResult] = {
            clients shouldBe "myclients"
            matchers shouldBe "csrfToken"        // Empty string means always matching hit in RequireAllMatchersChecker.java
            authorizers shouldBe "myauthorizers" // Empty string means always authorize in DefaultAuthorizationCheck.java
            multiProfile shouldBe false

            httpActionAdapter shouldBe actionAdapter
            Future.successful(Complete(HttpResponse(StatusCodes.OK, entity = "called!")))
          }
        })

        val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

        Get("/") ~> akkaHttpSecurity.withAuthentication(
          "myclients",
          multiProfile = false,
          authorizers = "myauthorizers"
        ) { _ => complete("problem!") } ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldBe "called!"
        }
      }
    }

    "calls inner route with authenticated profiles from directive when securityGrantedAccessAdapter is invoked and produces output" in {
      shade { implicit c =>
        val config = new Config()
        val profile = new UserProfile {
          override def getId: String = ""
        }
        new ForgetfulSessionStorage().index()

        config.setSecurityLogic((new AkkaHttpSecurityLogic {
          override def perform(
              context: AkkaHttpWebContext,
              config: Config,
              securityGrantedAccessAdapter: SecurityGrantedAccessAdapter[Future[RouteResult], AkkaHttpWebContext],
              httpActionAdapter: HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext],
              clients: String,
              authorizers: String,
              matchers: String,
              multiProfile: java.lang.Boolean,
              parameters: AnyRef*
          ): Future[RouteResult] = {
            securityGrantedAccessAdapter.adapt(context, List(profile).asJava)
          }
        }))

        val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)
        val route =
          akkaHttpSecurity.withAuthentication() { authenticated =>
            {
              authenticated.profiles.size shouldBe 1
              authenticated.profiles.head shouldBe profile
              complete("called!")
            }
          }

        Get("/") ~> route ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldBe "called!"
        }
      }
    }

    "AkkaHttpSecurity.withContext" should {
      "sets response headers when they are set in the context" in {
        shade { implicit c =>
          val config = new Config()
          new ForgetfulSessionStorage().index()
          val akkaHttpSecurity = inject[AuthnSAMLBuilder].Default

          Get("/") ~> akkaHttpSecurity.withContext() { context =>
            context.setResponseHeader("MyHeader", "MyValue")
            complete("called!")
          } ~> check {
            status shouldEqual StatusCodes.OK
            responseAs[String] shouldBe "called!"
            header("MyHeader").get.value() shouldBe "MyValue"
          }
        }
      }

      "sets response cookies (deduplicated) when they're set in the context" in {
        val config           = new Config()
        val akkaHttpSecurity = inject[AuthnSAMLBuilder].Default

        Get("/") ~> akkaHttpSecurity.withContext() { context =>
          val cookie = new Cookie("MyCookie", "MyValue")
          cookie.setSecure(true)
          cookie.setMaxAge(100)
          cookie.setHttpOnly(true)
          cookie.setPath("/")

          val cookie2 = new Cookie("MyCookie", "MyValue")
          cookie2.setSecure(true)
          cookie2.setMaxAge(100)
          cookie2.setHttpOnly(true)
          cookie2.setPath("/")

          context.addResponseCookie(cookie)
          context.addResponseCookie(cookie2)
          complete("called!")
        } ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldBe "called!"
          headers.size shouldBe 2
          header("Set-Cookie").get
            .value() shouldBe "MyCookie=MyValue; Max-Age=100; Path=/; Secure; HttpOnly; SameSite=None"
        }
      }

      "get request parameters from a form" in {
        val config           = new Config()
        val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

        val postRequest = HttpRequest(
          HttpMethods.POST,
          "/",
          entity = HttpEntity(
            ContentType(MediaTypes.`application/x-www-form-urlencoded`, () => HttpCharsets.`UTF-8`),
            "username=testuser".getBytes
          )
        )

        postRequest ~> akkaHttpSecurity.withFormParameters.apply { params =>
          params("username") shouldEqual "testuser"
          complete("called!")
        } ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldBe "called!"
        }
      }

      "fail a request when no parameters exist in a form and enforceFormEncoding is enabled" in {
        val config           = new Config()
        val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

        val postRequest = HttpRequest(
          HttpMethods.POST,
          "/",
          entity = HttpEntity(ContentType(MediaTypes.`application/json`), "".getBytes)
        )

        postRequest ~> akkaHttpSecurity.withFormParameters.apply { _ => fail("perform should never be called!") } ~> check {
          status shouldEqual StatusCodes.InternalServerError
        }
      }
    }

    "AkkaHttpSecurity.callback" should {
      "uses provided callbackLogic and pass the expected parameters" in {
        val config = new Config()

        val actionAdapter = new HttpActionAdapter[HttpResponse, AkkaHttpWebContext] {
          override def adapt(code: HttpAction, context: AkkaHttpWebContext): HttpResponse = ???
        }

        config.setHttpActionAdapter(actionAdapter)
        config.setCallbackLogic(new AkkaHttpCallbackLogic {
          override def perform(
              context: AkkaHttpWebContext,
              config: Config,
              httpActionAdapter: HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext],
              defaultUrl: String,
              saveInSession: java.lang.Boolean,
              multiProfile: java.lang.Boolean,
              renewSession: java.lang.Boolean,
              client: String
          ): Future[RouteResult] = {
            httpActionAdapter shouldBe actionAdapter
            defaultUrl shouldBe "/blaat"
            saveInSession shouldBe false
            multiProfile shouldBe false
            renewSession shouldBe true
            client shouldBe "Yooo"

            Future.successful(Complete(HttpResponse(StatusCodes.OK, entity = "called!")))
          }
        })

        val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

        Get("/") ~> akkaHttpSecurity.callback("/blaat", saveInSession = false, multiProfile = false, Some("Yooo")) ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldBe "called!"
        }
      }

      "run the callbackLogic reusing an akka http context" in {
        val config = new Config()
        val existingContext = new AkkaHttpWebContext(
          HttpRequest(),
          Map.empty,
          new ForgetfulSessionStorage()
        )(inject[SAMLAuthConfig], new SessionIDGenerator())
        new ForgetfulSessionStorage().index()

        val actionAdapter = new HttpActionAdapter[HttpResponse, AkkaHttpWebContext] {
          override def adapt(code: HttpAction, context: AkkaHttpWebContext): HttpResponse = ???
        }

        config.setHttpActionAdapter(actionAdapter)
        config.setCallbackLogic(new AkkaHttpCallbackLogic {
          override def perform(
              context: AkkaHttpWebContext,
              config: Config,
              httpActionAdapter: HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext],
              defaultUrl: String,
              saveInSession: java.lang.Boolean,
              multiProfile: java.lang.Boolean,
              renewSession: java.lang.Boolean,
              client: String
          ): Future[RouteResult] = {
            existingContext.sessionId shouldBe context.sessionId
            httpActionAdapter shouldBe actionAdapter
            defaultUrl shouldBe "/blaat"
            saveInSession shouldBe false
            multiProfile shouldBe false
            renewSession shouldBe true
            client shouldBe "Yooo"

            Future.successful(Complete(HttpResponse(StatusCodes.OK, entity = "called!")))
          }
        })

        val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

        Get("/") ~> akkaHttpSecurity.callback(
          "/blaat",
          saveInSession = false,
          multiProfile = false,
          Some("Yooo"),
          existingContext = Some(existingContext)
        ) ~> check {
          status shouldEqual StatusCodes.OK
          responseAs[String] shouldBe "called!"
        }
      }
    }

    "AkkaHttpSecurity.authorize" should {
      "pass the provided authenticationRequest to the authorizer" in {
        val profile = new CommonProfile()
        val context = new AkkaHttpWebContext(
          HttpRequest(),
          Map.empty,
          new ForgetfulSessionStorage()
        )(inject[SAMLAuthConfig], new SessionIDGenerator())

        val authorizer = new Authorizer[UserProfile] {
          override def isAuthorized(context: WebContext, profiles: util.List[UserProfile]): Boolean = {
            profiles.size() shouldBe 1
            profiles.get(0) shouldBe profile
            false
          }
        }
        val route: Route =
          AkkaHttpSecurity.auth(authorizer)(new AuthenticatedRequest(context, List(profile))) {
            complete("oops!")
          }

        (Get("/") ~> route).rejections shouldBe Seq(AuthorizationFailedRejection)
      }

      "reject when authorization fails" in {
        val context = new AkkaHttpWebContext(
          HttpRequest(),
          Map.empty,
          new ForgetfulSessionStorage()
        )(inject[SAMLAuthConfig], new SessionIDGenerator())

        val authorizer = new Authorizer[UserProfile] {
          override def isAuthorized(context: WebContext, profiles: util.List[UserProfile]): Boolean = false
        }
        val route =
          AkkaHttpSecurity.auth(authorizer)(new AuthenticatedRequest(context, List.empty)) {
            complete("oops!")
          }

        (Get("/") ~> route).rejections shouldBe Seq(AuthorizationFailedRejection)
      }

      "succeed when authorization succeeded" in {
        val context = new AkkaHttpWebContext(
          HttpRequest(),
          Map.empty,
          new ForgetfulSessionStorage()
        )(inject[SAMLAuthConfig], new SessionIDGenerator())

        val authorizer = new Authorizer[UserProfile] {
          override def isAuthorized(context: WebContext, profiles: util.List[UserProfile]): Boolean = true
        }
        val route =
          AkkaHttpSecurity.auth(authorizer)(new AuthenticatedRequest(context, List.empty)) {
            complete("cool!")
          }

        Get("/") ~> route ~> check {
          status shouldBe StatusCodes.OK
          entityAs[String] shouldBe "cool!"
        }
      }
    }

    "AkkaHttpSecurity.logout" should {
      "run the callbackLogic with the expected parameters" in {
        shade { implicit c =>
          val config = new Config()

          config.setHttpActionAdapter(AkkaHttpActionAdapter)
          config.setLogoutLogic(new AkkaHttpLogoutLogic {
            override def perform(
                context: AkkaHttpWebContext,
                config: Config,
                httpActionAdapter: HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext],
                defaultUrl: String,
                logoutUrlPattern: String,
                localLogout: java.lang.Boolean,
                destroySession: java.lang.Boolean,
                centralLogout: java.lang.Boolean
            ): Future[RouteResult] = {
              httpActionAdapter shouldBe AkkaHttpActionAdapter
              defaultUrl shouldBe "/home"
              logoutUrlPattern shouldBe "*"
              localLogout shouldBe false
              destroySession shouldBe false

              Future.successful(Complete(HttpResponse(StatusCodes.OK, entity = "logout!")))
            }
          })

          new ForgetfulSessionStorage().index()

          val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

          Get("/") ~> akkaHttpSecurity.logout("/home", "*", localLogout = false, destroySession = false) ~> check {
            status shouldEqual StatusCodes.OK
            responseAs[String] shouldBe "logout!"
          }
        }
      }

      "destroy the session and create a new empty one" in {
        shade { implicit c =>
          val config = new Config()
          new InMemorySessionStorage(inject[ScalaTime], inject[SAMLAuthConfig]) {
            override def getTime: Long = 86400 * 3
          }.index[SessionStorage]()

          val client = new IndirectClient[UsernamePasswordCredentials] {
            override def clientInit(): Unit = ???
          }

          val logoutLogic = new DefaultLogoutLogic[Future[RouteResult], AkkaHttpWebContext] {
            override def perform(
                context: AkkaHttpWebContext,
                config: Config,
                httpActionAdapter: HttpActionAdapter[Future[RouteResult], AkkaHttpWebContext],
                defaultUrl: String,
                inputLogoutUrlPattern: String,
                inputLocalLogout: java.lang.Boolean,
                inputDestroySession: java.lang.Boolean,
                inputCentralLogout: java.lang.Boolean
            ): Future[RouteResult] = {

              val profile = new UserProfile {
                override def getId: String = "Profile"
              }
              context.sessionStorage.setSessionValue(
                context.sessionId,
                Pac4jConstants.USER_PROFILES,
                Map("foo" -> profile).asJava
              )
              context.sessionStorage.getSessionValue(context.sessionId, Pac4jConstants.USER_PROFILES) contains Map(
                "foo" -> profile
              ).asJava

              val response = super.perform(
                context,
                config,
                httpActionAdapter,
                defaultUrl,
                inputLogoutUrlPattern,
                inputLocalLogout,
                inputDestroySession,
                inputCentralLogout
              )
              context.sessionStorage.getSessionValue(context.sessionId, Pac4jConstants.USER_PROFILES) shouldBe None

              response
            }
          }

          config.setHttpActionAdapter(AkkaHttpActionAdapter)
          config.setLogoutLogic(logoutLogic)
          config.setClients(new Clients("url", client))

          val akkaHttpSecurity = inject[AuthnSAMLBuilder].build(config)

          Get("/") ~> akkaHttpSecurity.logout("/home", "*") ~> check {
            status shouldEqual StatusCodes.SeeOther
            header("Location").map(_.value) shouldBe Some("/home")
          }
        }
      }
    }
  }
}
