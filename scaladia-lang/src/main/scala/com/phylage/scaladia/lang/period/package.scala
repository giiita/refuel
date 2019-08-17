package com.phylage.scaladia.lang

import ScalaTime._

package object period {
  type EpochDateTime = Long

  case object DAY extends TimeAxis {

    /**
      * Time incrementer
      *
      * @param value Existing time
      * @param at    Increment size
      * @return
      */
    override def increment(value: EpochDateTime, at: Long): EpochDateTime = value.datetime.plusDays(at).toEpochSecond

    /**
      * 時間丸め処理
      *
      * @param value 時間
      * @return
      */
    override def rounding(value: EpochDateTime): EpochDateTime = value.datetime.minusSeconds(1).toEpochSecond
  }

  case object HOUR extends TimeAxis {

    /**
      * Time incrementer
      *
      * @param value Existing time
      * @param at    Increment size
      * @return
      */
    override def increment(value: EpochDateTime, at: Long): EpochDateTime = value.datetime.plusHours(at).toEpochSecond

    /**
      * 時間丸め処理
      *
      * @param value 時間
      * @return
      */
    override def rounding(value: EpochDateTime): EpochDateTime = value.datetime.minTohour.minusSeconds(1).toEpochSecond
  }

}
