package refuel.oauth.token

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import refuel.oauth.endpoint.TokenEndpoint
import refuel.oauth.exception.RequirementFailedException

import scala.util.Try

sealed abstract class GrantRequest(
    val clientId: String,
    val clientSecret: String,
    val parameters: Map[String, String]
) {

  def require(key: String): Either[Throwable, String] =
    parameters.get(key).toRight(new RequirementFailedException(s"$key is required but was no found."))

  def maybe(key: String): Option[String] = parameters.get(key)
}

object GrantRequest {

  class Raw private (
      clientId: String,
      clientSecret: String,
      parameters: Map[String, String]
  ) extends GrantRequest(clientId, clientSecret, parameters)

  class AuthorizeCodeGrantRequest(
      clientId: String,
      clientSecret: String,
      parameters: Map[String, String],
      val code: String,
      val redirectUri: Option[Uri],
      val codeVerifier: Option[String]
  ) extends GrantRequest(clientId, clientSecret, parameters)

  @deprecated("Since OAuth2.1, this specification seems to disappear.")
  class PasswordGrantRequest(
      clientId: String,
      clientSecret: String,
      parameters: Map[String, String],
      val username: String,
      val password: String
  ) extends GrantRequest(clientId, clientSecret, parameters)

  class ClientCredentialsGrantRequest(
      clientId: String,
      clientSecret: String,
      parameters: Map[String, String]
  ) extends GrantRequest(clientId, clientSecret, parameters)

  class RefreshTokenGrantRequest(
      clientId: String,
      clientSecret: String,
      parameters: Map[String, String],
      val refreshToken: String
  ) extends GrantRequest(clientId, clientSecret, parameters)

  object Raw {
    def apply(creds: BasicHttpCredentials, fields: Map[String, String]): Raw =
      new Raw(creds.username, creds.password, fields)
  }

  object AuthorizeCodeGrantRequest {
    def apply(req: GrantRequest): Try[AuthorizeCodeGrantRequest] = {
      for {
        _code     <- req.require(TokenEndpoint.Constant.Code)
        _redirect <- Try { req.maybe(TokenEndpoint.Constant.RedirectUri).map(Uri(_)) }.toEither
      } yield new AuthorizeCodeGrantRequest(
        req.clientId,
        req.clientSecret,
        req.parameters,
        _code,
        _redirect,
        req.maybe(TokenEndpoint.Constant.CodeVerifier)
      )
    }.toTry
  }

  @deprecated("Since OAuth2.1, this specification seems to disappear.")
  object PasswordGrantRequest {
    def apply(req: GrantRequest): Try[PasswordGrantRequest] = {
      for {
        _username <- req.require(TokenEndpoint.Constant.Username)
        _password <- req.require(TokenEndpoint.Constant.Password)
      } yield {
        new PasswordGrantRequest(req.clientId, req.clientSecret, req.parameters, _username, _password)
      }
    }.toTry
  }

  object RefreshTokenGrantRequest {
    def apply(req: GrantRequest): Try[RefreshTokenGrantRequest] = {
      for {
        _refreshToken <- req.require(TokenEndpoint.Constant.RefreshToken)
      } yield new RefreshTokenGrantRequest(req.clientId, req.clientSecret, req.parameters, _refreshToken)
    }.toTry
  }

  object ClientCredentialsGrantRequest {
    def apply(req: GrantRequest): Try[ClientCredentialsGrantRequest] = Try {
      new ClientCredentialsGrantRequest(req.clientId, req.clientSecret, req.parameters)
    }
  }
}
