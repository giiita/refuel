package refuel.inject

import scala.quoted._

object InjectableTag {
  def apply()(using q: Quotes) = {
    import q.reflect._
    TypeTree.of[AutoInject]
  }
}
