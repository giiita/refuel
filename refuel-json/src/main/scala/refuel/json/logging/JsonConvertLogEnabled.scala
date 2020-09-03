package refuel.json.logging

import com.typesafe.config.ConfigFactory

case class JsonConvertLogEnabled(enabled: Boolean) extends AnyVal

object JsonConvertLogEnabled {
  lazy final val Default = {
    val conf = ConfigFactory.load()
    JsonConvertLogEnabled(
      if (conf.hasPath("json.logging.enabled")) conf.getBoolean("json.logging.enabled") else false
    )
  }
}
