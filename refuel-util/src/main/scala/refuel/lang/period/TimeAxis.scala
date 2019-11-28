package refuel.lang.period

trait TimeAxis {
  /**
    * Time unit down
    *
    * @param value Existing time
    * @return
    */
  def down(value: EpochDateTime): EpochDateTime

  /**
    * Time incrementer
    *
    * @param value Existing time
    * @param at    Increment size
    * @return
    */
  def increment(value: EpochDateTime, at: Long): EpochDateTime

  /**
    * Time rounding
    *
    * @param value 時間
    * @return
    */
  def rounding(value: EpochDateTime): EpochDateTime

  /**
    * Time derounding
    *
    * @param value 時間
    * @return
    */
  def derounding(value: EpochDateTime): EpochDateTime
}
