package refuel

import org.pac4j.core.profile.UserProfile

/**
  * Class which serves as a witness when authentication was successful. This class is private such that the user of this
  * library cannot mess with it.
  */
class AuthenticatedRequest private[refuel] (
    private[refuel] val _webContext: AkkaHttpWebContext,
    _profiles: List[UserProfile]
) {
  def profiles: List[UserProfile] = _profiles
  def mainProfile: UserProfile    = _profiles.head
}
