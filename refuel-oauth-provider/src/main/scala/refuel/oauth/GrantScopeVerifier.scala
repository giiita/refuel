package refuel.oauth

import scala.concurrent.Future

/** Validate the requested authorization scope.
  * If the authorization scope itself is invalid, return Failure. If subsequent
  * processing is to continue, create your own defined GrantScope.
  *
  * Depending on the specification, for example, if you receive an unsupported authorization
  * scope request, or if you are expecting one or more authorization scopes and there is no request,
  * you can notify the authorization redirection endpoint of the error by returning Failure as an invalid request.
  *
  * Require: Mixin AutoInject
  */
trait GrantScopeVerifier extends (Iterable[String] => Future[Iterable[GrantScope]]) {}
