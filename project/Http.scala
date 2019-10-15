import java.util.logging.{Level, Logger}

import scala.util.{Failure, Success, Try}

object Http {

  import skinny.http._

  def connect(uri: String): Unit = {

    def circleCall: Unit = {
      Thread.sleep(1000)

      Try {
        HTTP.request(Method.GET, new Request(uri)).status match {
          case 200 => Logger.getLogger("").log(Level.INFO, "HTTP server standby.")
          case s   =>
            Logger.getLogger("").log(Level.INFO, s"$uri [ $s ] HTTP server stand not yet...")
            circleCall
        }
      } match {
        case Success(_) => ()
        case Failure(e) =>
          Logger.getLogger("").log(Level.INFO, s"$uri [ ${e.getMessage} ] HTTP server stand not yet...")
          circleCall
      }
    }

    circleCall
  }
}
