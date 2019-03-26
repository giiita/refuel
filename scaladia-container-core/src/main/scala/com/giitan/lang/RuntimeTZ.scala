package com.giitan.lang

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId, ZoneOffset}
import java.util.TimeZone

object RuntimeTZ extends RuntimeTZ {
  override val TIME_ZONE: TimeZone = TimeZone.getDefault
  override val ZONE_ID: ZoneId = java.time.ZoneId.systemDefault()
  override val ZONE_OFFSET: ZoneOffset = ZONE_ID.getRules.getOffset(Instant.now())
  override val DEFAULT_FORMAT: String = "yyyy/M/d H:m:s Z"
}

/**
  * When using a fixed time zone
  * ```object MyTZ extends AsiaTokyoTZ with AutoInject[RuntimeTZ] ```
  */
class AsiaTokyoTZ extends RuntimeTZ {
  override val TIME_ZONE: TimeZone = TimeZone.getTimeZone("Asia/Tokyo")
  override val ZONE_ID: ZoneId = ZoneId.of("Asia/Tokyo")
  override val ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)
  override val DEFAULT_FORMAT: String = RuntimeTZ.DEFAULT_FORMAT
}

/**
  * Runtime timezone settings.
  * By first overwriting, you can change the TimeZone handled by ScalaTime.
  */
trait RuntimeTZ { me =>
  val TIME_ZONE: TimeZone
  /* Zone id */
  val ZONE_ID: ZoneId
  /* Zone offset */
  val ZONE_OFFSET: ZoneOffset
  /* Default String format */
  val DEFAULT_FORMAT: String

  TimeZone.setDefault(me.TIME_ZONE)

  lazy val format: DateTimeFormatter = DateTimeFormatter.ofPattern(me.DEFAULT_FORMAT)
}
