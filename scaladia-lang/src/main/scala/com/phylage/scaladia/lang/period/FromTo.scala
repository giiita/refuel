package com.phylage.scaladia.lang.period

import scala.annotation.tailrec

/**
  * Interface representing period.
  */
trait FromTo {
  val from: EpochDateTime
  val to: EpochDateTime

  /**
    * Determine if two periods overlap.
    *
    * {{{
    *   case class MyPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo
    *
    *   val period = MyPeriod("2019-08-01 00:00:00", "2019-08-01 11:59:59")
    *
    *   period.overlap(MyPeriod("2019-07-31 00:00:00", "2019-07-31 23:59:59")) // be false
    *   period.overlap(MyPeriod("2019-07-31 00:00:00", "2019-08-01 00:00:00")) // be true
    * }}}
    *
    * @param arg Another period.
    * @return
    */
  def overlap[T <: FromTo](arg: T): Boolean = {
    arg.contains(from) || arg.contains(to) || this.contains(arg.from) || this.contains(arg.to)
  }

  /**
    * Check if this time is included in this period.
    *
    * {{{
    *   case class MyPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo
    *
    *   val period = MyPeriod("2019-08-01 00:00:00", "2019-08-01 11:59:59")
    *
    *   period.contains("2019-07-31 23:59:59") // be false
    *   period.contains("2019-08-01 00:00:00") // be true
    *   period.contains("2019-08-01 11:59:59") // be true
    *   period.contains("2019-08-01 12:00:00") // be false
    * }}}
    *
    * @param arg 特定の時間
    * @return
    */
  def contains(arg: EpochDateTime): Boolean = {
    arg >= from && arg <= to
  }

  /**
    * Breaks down the period in arbitrary units.
    * {{{
    *   case class MyPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo
    *
    *   MyPeriod("2019-08-01".datetime.epoch, "2019-08-03 23:59:59".datetime.epoch).slice(12, HOUR)(MyPeriod)
    *    == Seq(
    *      MyPeriod("2019-08-01 00:00:00", "2019-08-01 11:59:59"),
    *      MyPeriod("2019-08-01 12:00:00", "2019-08-01 23:59:59"),
    *      MyPeriod("2019-08-02 00:00:00", "2019-08-02 11:59:59"),
    *      MyPeriod("2019-08-02 12:00:00", "2019-08-02 23:59:59"),
    *      MyPeriod("2019-08-03 00:00:00", "2019-08-03 11:59:59"),
    *      MyPeriod("2019-08-03 12:00:00", "2019-08-03 23:59:59")
    *    )
    * }}}
    *
    * @param sliceAt   Size to slice
    * @param time      Unit to slice
    * @param applyment Recursively decomposed period
    * @tparam X Unit to increments
    * @tparam T period type
    * @return
    */
  def slice[X <: TimeAxis, T <: FromTo](sliceAt: Long, time: X = HOUR)(applyment: (EpochDateTime, EpochDateTime) => T): Seq[T] = {
    _slice(sliceAt, time)(applyment)
  }

  /**
    * Breaks down the period in arbitrary units.
    *
    * @param sliceAt    Size to slice.
    * @param time       Unit to slice.
    * @param result     Recursively decomposed period.
    * @param applyement contructor
    * @tparam X Unit to increments.
    * @tparam T period type.
    * @return
    */
  @tailrec
  private[this] final def _slice[X <: TimeAxis, T <: FromTo](sliceAt: Long, time: X = HOUR, result: Seq[T] = Nil)(applyement: (EpochDateTime, EpochDateTime) => T): Seq[T] = {
    val nextFrom = result.lastOption.map(x => time.derounding(x.to)).getOrElse(from)
    time.increment(nextFrom, sliceAt) match {
      case nextTo if nextTo >= to => result :+ applyement(nextFrom, to)
      case nextTo => _slice(sliceAt, time, result :+ applyement(nextFrom, time.rounding(nextTo)))(applyement)
    }
  }
}