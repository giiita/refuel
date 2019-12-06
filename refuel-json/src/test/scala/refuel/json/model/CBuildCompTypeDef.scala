package refuel.json.model

object CBuildCompTypeDef {
  case class A(test1: String, test2: String, test3: Option[String])

  case class AA(value: String)

  case class B(a1: A, a2: A, aa1: Option[AA], aa2: Option[AA])

  case class C(a: B)

  case class Depth3LineA(value: String)

  case class Depth2LineA(test1: Depth3LineA, test2: Depth3LineA, test3: Option[Depth3LineA])

  case class Depth2LineB(test1: Depth3LineA)

  case class Depth1(v1: Depth2LineA, v2: Depth2LineA, v3: Option[Depth2LineB], v4: Option[Depth2LineB])
}
