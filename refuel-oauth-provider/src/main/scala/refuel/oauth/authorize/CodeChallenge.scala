package refuel.oauth.authorize

import java.security.MessageDigest
import java.util.Base64

import refuel.oauth.authorize.CodeChallenge.CodeChallengeMethod
import refuel.oauth.concurrent.FutureAuthorizeEndpoint
import refuel.oauth.exception.{InvalidGrantException, RequirementFailedException}

import scala.annotation.switch
import scala.util.{Failure, Success, Try}

case class CodeChallenge private (value: String, method: CodeChallengeMethod) {
  def verify(verifier: String): Boolean = {
    method.computeChallenge(verifier) == value
  }
}

object CodeChallenge {

  def fromRequest(req: Map[String, String]): Try[Option[CodeChallenge]] = {
    req.get(FutureAuthorizeEndpoint.Constant.CodeChallenge) -> req.get(
      FutureAuthorizeEndpoint.Constant.CodeChallengeMethod
    ) match {
      case (Some(challenge), Some(method)) => pure(challenge, method).map(Some(_))
      case (None, None)                    => Success(None)
      case _ =>
        Failure(
          new RequirementFailedException(
            "Both code_challenge and code_challenge_method must be specified, or none of them"
          )
        )
    }
  }

  def pure(value: String, method: String): Try[CodeChallenge] = {
    (method: @switch) match {
      case "S256"  => Success(S256)
      case "plain" => Success(Plain)
      case _       => Failure(new InvalidGrantException("Unsupported challenge code method."))
    }
  }.map(CodeChallenge(value, _))

  sealed trait CodeChallengeMethod {
    def name: String
    def computeChallenge: String => String
  }

  case object S256 extends CodeChallengeMethod {
    def name: String = "S256"
    def computeChallenge: String => String = { x =>
      val codeVerifierBytes = x.getBytes("ASCII")
      val digest            = MessageDigest.getInstance("SHA-256").digest(codeVerifierBytes)
      Base64.getUrlEncoder.withoutPadding().encodeToString(digest)
    }
  }
  case object Plain extends CodeChallengeMethod {
    def name: String                       = "plain"
    def computeChallenge: String => String = x => x
  }
}
