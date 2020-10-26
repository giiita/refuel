# refuel-saml-provider

```
libraryDependencies += "com.phylage" %% "refuel-saml-provider" % "1.4.0"
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
  sp-metadata-path = "" # optional
  authn-request-binding-type = "" # default "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST",
  callbackurl = "" required
  session-cookie-name = "" # default Pac4jConstants.SESSION_ID
  lifetime-seconds = 86400 # default 86400 * 3
  cookie-path = "" # default Pac4jConstants.DEFAULT_URL_VALUE
  csrf-token-key = "" # default Pac4jConstants.CSRF_TOKEN
}
```

##### MyController.scala

```scala
class MyController(builder: AkkaHttpSecurityBuilder)(implicit actorSystem: ActorSystem) extends Directives with AutoInject {
  def xxx: Route = path("xxx") {
    builder.Default.clientsAuthentication() { _: AuthenticatedRequest =>
      StatusCodes.OK
    }
  }
}
```

```scala
implicit val as: ActorSystem = ActorSystem()
Http().bindAndHandle(inject[MyController].xxx)
```