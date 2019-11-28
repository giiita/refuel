package refuel.json.model

object TestJson {
  case class JInt(value: Int)
  case class JLong(value: Long)
  case class JString(value: String)
  case class JFloat(value: Float)
  case class JDouble(value: Double)
  case class JBoolean(value: Boolean)

  case class JOptInt(value: Option[Int])
  case class JOptLong(value: Option[Long])
  case class JOptString(value: Option[String])
  case class JOptFloat(value: Option[Float])
  case class JOptDouble(value: Option[Double])
  case class JOptBoolean(value: Option[Boolean])

  case class JTuple2(value: (Int, Int))
  case class JTuple22(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))

  class HasNotApply(value: JLong)
}