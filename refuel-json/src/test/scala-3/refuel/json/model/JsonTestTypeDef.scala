package refuel.json.model

object JsonTestTypeDef {
  case class A(test1: String, test2: String, test3: Option[String])

  case class AA(value: String)

  case class B(a1: A, a2: A, aa1: Option[AA], aa2: Option[AA])

  case class C(a: B)

  case class Depth3LineA(value: String)

  case class Depth2LineA(test1: Depth3LineA, test2: Depth3LineA, test3: Option[Depth3LineA])

  case class Depth2LineB(test1: Depth3LineA)

  case class Depth1(v1: Depth2LineA, v2: Depth2LineA, v3: Option[Depth2LineB], v4: Option[Depth2LineB])

  case class String4(v1: String, v2: String, v3: String, v4: String)
  case class String4_2(v1: String4, v2: Option[String4])
}
