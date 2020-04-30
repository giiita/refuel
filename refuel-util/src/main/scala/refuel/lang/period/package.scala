package refuel.lang

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
      * Time rounding
      *
      * @param value Existing time
      * @return
      */
    override def rounding(value: EpochDateTime): EpochDateTime = value.datetime.minusDays(1).maxToday.toEpochSecond

    /**
      * Time deround
      *
      * @param value Existing time
      * @return
      */
    override def derounding(value: EpochDateTime): EpochDateTime = value.datetime.plusDays(1).minToday.toEpochSecond

    /**
      * Time unit down
      *
      * @param value Existing time
      * @return
      */
    override def down(value: EpochDateTime): EpochDateTime = value.datetime.minToday.epoch
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
      * Time rounding
      *
      * @param value Existing time
      * @return
      */
    override def rounding(value: EpochDateTime): EpochDateTime = value.datetime.minusHours(1).maxTohour.toEpochSecond

    /**
      * Time derounding
      *
      * @param value Existing time
      * @return
      */
    override def derounding(value: EpochDateTime): EpochDateTime = value.datetime.plusHours(1).minTohour.toEpochSecond

    /**
      * Time unit down
      *
      * @param value Existing time
      * @return
      */
    override def down(value: EpochDateTime): EpochDateTime = value.datetime.minTohour.epoch
  }

}
