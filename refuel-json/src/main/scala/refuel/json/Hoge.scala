package refuel.json

object Hoge {


  case class Name(first: String, last: String)

  case class Friend(id: Int, name: String)

  case class Hoge(_id: String,
                  index: Int,
                  guid: String,
                  isActive: Boolean,
                  balance: String,
                  picture: String,
                  age: Int,
                  eyeColor: String,
                  name: Name,
                  company: String,
                  email: String,
                  phone: String,
                  address: String,
                  about: String,
                  registered: String,
                  latitude: Float,
                  longitude: Float,
                  tags: Seq[String],
                  range: Set[Long],
                  friends: Set[Friend],
                  greeting: String)

  case class Root(root: Seq[Hoge])
}
