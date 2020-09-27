package refuel.http.io.setting

import com.typesafe.config.ConfigFactory

case class HttpClientLogEnabled(enabled: Boolean) extends AnyVal

object HttpClientLogEnabled {
  lazy final val Default = {
    val conf = ConfigFactory.load()
    HttpClientLogEnabled(
      if (conf.hasPath("http-client.logging.enabled")) conf.getBoolean("http-client.logging.enabled") else false
    )
  }
}
