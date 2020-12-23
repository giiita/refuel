package refuel.oauth.authorize

import scala.annotation.switch
import scala.util.Try

sealed trait ResponseType {
  def name: String
}

object ResponseType {

  def apply(`type`: String): Try[ResponseType] = Try {
    (`type`: @switch) match {
      case "code"  => Code
      case "token" => Token
      case other   => Extended(other)
    }
  }

  case object Code extends ResponseType {
    def name = "code"
  }
  case object Token extends ResponseType {
    def name = "token"
  }
  case class Extended(name: String) extends ResponseType
}
