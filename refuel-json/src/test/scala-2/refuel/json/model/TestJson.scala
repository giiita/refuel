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
  case class JTuple3(value: (Int, Int, Int))
  case class JTuple4(value: (Int, Int, Int, Int))
  case class JTuple5(value: (Int, Int, Int, Int, Int))
  case class JTuple6(value: (Int, Int, Int, Int, Int, Int))
  case class JTuple7(value: (Int, Int, Int, Int, Int, Int, Int))
  case class JTuple8(value: (Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple9(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple10(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple11(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple12(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple13(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple14(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple15(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple16(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple17(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple18(value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int))
  case class JTuple19(
      value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)
  )
  case class JTuple20(
      value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)
  )
  case class JTuple21(
      value: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)
  )
  case class JTuple22(
      value: (
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int,
          Int
      )
  )

  class HasNotApply(value: JLong)
}
