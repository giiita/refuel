## refuel-akka-oauth-provider

```
libraryDependencies += "com.phylage" %% "refuel-oauth-provider" % "1.4.11"
```

It provides an authorization process directive that follows the standard features of OAuth 2.0 / 2.1.
The Refuel oauth provider is not configured to strictly define the caller interface and force users to define it.
In addition, it is basically a direct extension style, which allows for flexible implementation of authorization servers.

Since the core architecture uses refuel-container, all service class constructors are automated by refuel.
Therefore, at the top level, we need to call inject for DI deployment.

```scala
Http().bindAndHandle(existRoute ~ inject[MyOAuthRoute], interface, port)
```

These are the supported grant types.

```
Authorization code grant
Client credential grant
Implicit grant (Not supported since OAuth2.1)
Password grant (Not supported since OAuth2.1)
Refresh token grant
```

## Authorization code grant flow

#### OAuth grant router
```scala
class MyAuthorizeController(override val authorize: AuthorizeEndpoint[Future],
                            override val token: TokenEndpoint[Future],
                            override val grantHandler: GrantHandler[Future, MyUser]) extends OAuth2[MyUser] {
  def route: Route = {
    concat(
      `GET_authorize`,
      `POST_token`
    )
  }
  
  /** Because it is redirection-based, it requires a browser transition from the client.
    * (Ajax is also available, but it requires intentional rendering on the client side)
    * 
    * /authorize?response_type=code&client_id=xxx&state=xxx
    *       &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
    */
  private[this] def `GET_authorize` = get {
    authorizeRequest { req =>
      // - If authentication and authorization cannot be confirmed, request authorization from the resource owner
      if (???) { // Authorization Require implementation
        // - Generate authorize code
        // - (optional) Store code
        authorizeCodeComplete(req, generatedCode)
      } else { // When unauthorized
        HttpResponse(StatusCodes.Found, headers = Seq(Location(Uri("/my/system/authenticate_and_authorize"))))
      }
    }
  }
  
  /**
    * Authorization: Basic base64(clientId:clientSecret)
    * Content-Type: application/x-www-form-urlencoded
    * 
    * grant_type=???&code=xxx
    * &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
    */
  private[this] def `POST_token` = post {
    grantRequest { req =>
      val accessToken = ???
      issue(accessToken)
    }
  }
}
```

#### Protected resource router

```scala
class MyProtectedController(override val protection: ProtectionEndpoint[Future],
                            override val grantHandler: GrantHandler[Future, U],
                            override val actionHandler: OAuth2ActionHandler) extends OAuth2Protection[Future, MyUser] {
  def route: Route = {
    concat(
      `GET_profile`
    )
  }
  
  /** Authorization: Bearer ${token}
    */
  private[this] def `GET_profile` = get {
    oauth2 { profile =>
      if (permitted(profile)) {
        ???
      }
    }
  }
}
```

#### GrantHandler
```scala
class MyGrantHandler extends GrantHandler[Future, MyUser] {
  def findApp(clientId: String): Future[AuthorizedApp] = {
    DB.select(clientId) // example
  }

  // Validate the requested grant scope.
  // Depending on the implementation, it can ignore or raise an error if the scope is not predefined.
  def verifyGrantScope(request: Iterable[String]): Future[Iterable[GrantScope]] = {
    request.flatMap(MyGrantScopes.apply) // example
  }
  
  // Restore the authorization request information from the authorization code.
  // This is required by OAuth 2.0 / 2.1 to be checked at the token endpoint.
  def restoreAuthorization(authorizeCode: String): Future[AuthorizeState[U]] = {
    NoSQL.get(authorizeCode) // example
  }
 
  // Checked when accessing protected resources with an access token.
  // The content of the test must be defined independently.
  def verifyAccessToken(accessToken: String): Future[AuthProfile[U]] = {
    NoSQL.get(accessToken) // example
  }
 

  // All GrantTypes other than the one you define will be Unsupported and no token will be issued.
  def verifyCredentials: PartialFunction[GrantRequest, Future[AuthProfile[U]]] = {
    // example
    case r: AuthorizeCodeGrantRequest => ???
    case r: RefreshTokenGrantRequest => ???
  }
}
```

By default, when an anomaly is detected on the OAuth flow, it will be auto completed by the ActionHandler defined in oauth-provider, but it can be customized.

```scala
class MyActionHandler extends AutoInject {
  def oauthActionComplete(action: HttpAction): ToResponseMarshallable = action match {
    case RedirectAction(_, cause) if cause.nonEmpty =>
      logger.error("OAuth2 failure and redirect.", cause.get)
      action.result
    case UnauthorizedAction(cause) =>
      logger.error("OAuth2 failure.", cause)
      action.result
    case other => other.result
  }
}
```