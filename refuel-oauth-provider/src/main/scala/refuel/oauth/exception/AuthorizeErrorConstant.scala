package refuel.oauth.exception

object AuthorizeErrorConstant {
  object Errors {
    lazy final val InvalidRequest          = "invalid_request"
    lazy final val UnauthorizedClient      = "unauthorized_client"
    lazy final val UnsupportedResponseType = "unsupported_response_type"
    lazy final val AccessDenied            = "access_denied"
    lazy final val InvalidScope            = "invalid_scope"
    lazy final val ServerError             = "server_error"
    lazy final val TemporarilyUnavailable  = "temporarily_unavailable"
  }

  object ErrorDescriptions {
    lazy final val ChallengeSessionExpired  = "challenge_session_expired"
    lazy final val AuthenticateVerifyFailed = "authenticate_verify_failed"
  }
}
