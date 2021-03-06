# refuel-saml-provider

```
libraryDependencies += "com.phylage" %% "refuel-auth-provider" % "1.5.2"
```

Provides SAML 2.0 service provider support for Akka http. The refuel framework will be worked on to support Scala 3.

##### application.conf
```config
saml {
  # Create automatically if it doesn't exist and you have write access
  keystore-path = "" # required
  keystore-password = "" # required
  private-key-password = "" # required
  # You need to download the metadata from the identity provided
  idp-metadata-path = "" # required
  sp-metadata-path = "" # (optional) 
  authn-request-binding-type = "" # (optional) default "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST",
  callbackurl = "" required
  session-cookie-name = "" # default Pac4jConstants.SESSION_ID
  lifetime-seconds = 86400 # default 86400 * 3
  cookie-path = "" # default Pac4jConstants.DEFAULT_URL_VALUE
  cookie-secure = true # (optional) default true
  cookie-domain = "xxx.com" # (optional) default requested domain
  cookie-extension = "foo=bar" # (optional) default SameSite=None;
  force-redirect-scheme = "https" # (optional) default https;
  csrf-token-key = "" # default Pac4jConstants.CSRF_TOKEN
}
```

##### MyController.scala

```scala
class MyController(builder: AuthnSAMLBuilder)(implicit actorSystem: ActorSystem) extends Directives with AutoInject {
  def staged: Route = path("staged") {
    get {
      StatusCodes.OK
    }
  }

  def logout: Route = path("logout") {
    get {
      builder.Default.logout("/loggedout")
    }
  }

  // ログアウトが完了した
  def loggedout: Route = path("loggedout") {
    get {
      StatusCodes.OK
    }
  } 

  def stage: Route = path("stage") {
    post {
      builder.Default.callback("/staged")
    }
  }

  def anyAction: Route = path("do_???") {
    get {
      builder.Default.clientsAuthentication() { x: AuthenticatedRequest =>
        StatusCodes.OK -> x.profiles.toJString
      }
    }
  }
}
```

```scala
implicit val as: ActorSystem = ActorSystem()
Http().bindAndHandle(inject[MyController].xxx)
```