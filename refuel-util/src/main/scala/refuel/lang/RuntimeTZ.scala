package refuel.lang

import com.typesafe.config.ConfigFactory
import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId, ZoneOffset}
import java.util.TimeZone

/**
  * TimeZone used by default.
  */
@Inject[Finally]
object SystemDefault extends RuntimeTZ with AutoInject {
  override val TimeZone: TimeZone     = java.util.TimeZone.getDefault()
  override val ZoneId: ZoneId         = java.time.ZoneId.systemDefault()
  override val ZoneOffset: ZoneOffset = ZoneId.getRules.getOffset(Instant.now())
  override val Format: DateTimeFormatter  = {
    val conf = ConfigFactory.load()
    val path = "datetime.default-format"
    if (conf.hasPathOrNull(path) && conf.getIsNull(path)) DateTimeFormatter.ofPattern(conf.getString(path)) else DateTimeFormatter.ISO_ZONED_DATE_TIME
  }
}

/**
  * When using a fixed time zone
  * {{{
  * object MyTZ extends AsiaTokyoTZ with AutoInject
  * }}}
  */
class AsiaTokyo extends RuntimeTZ {
  override val TimeZone: TimeZone     = java.util.TimeZone.getTimeZone("Asia/Tokyo")
  override val ZoneId: ZoneId         = java.time.ZoneId.of("Asia/Tokyo")
  override val ZoneOffset: ZoneOffset = java.time.ZoneOffset.ofHours(9)
  override val Format: DateTimeFormatter  = SystemDefault.Format
}

class EtcUtc extends RuntimeTZ {
  override val TimeZone: TimeZone     = java.util.TimeZone.getTimeZone("Etc/UCT")
  override val ZoneId: ZoneId         = java.time.ZoneId.of("Etc/UCT")
  override val ZoneOffset: ZoneOffset = java.time.ZoneOffset.ofHours(0)
  override val Format: DateTimeFormatter  = SystemDefault.Format
}

/** Runtime timezone settings.
  * By first overwriting, you can change the TimeZone handled by ScalaTime.
  */
trait RuntimeTZ { me =>
  /* Time zone */
  val TimeZone: TimeZone
  /* Zone id */
  val ZoneId: ZoneId
  /* Zone offset */
  val ZoneOffset: ZoneOffset
  /* Default String format */
  val Format: DateTimeFormatter
}
