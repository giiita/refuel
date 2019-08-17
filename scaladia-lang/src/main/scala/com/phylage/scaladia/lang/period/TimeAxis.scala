package com.phylage.scaladia.lang.period

trait TimeAxis {
  /**
    * Time incrementer
    *
    * @param value Existing time
    * @param at    Increment size
    * @return
    */
  def increment(value: EpochDateTime, at: Long): EpochDateTime

  /**
    * 時間丸め処理
    *
    * @param value 時間
    * @return
    */
  def rounding(value: EpochDateTime): EpochDateTime
}
