package refuel.json

import org.openjdk.jmh.annotations.{Benchmark, State}

import scala.annotation.{switch, tailrec}

@State(org.openjdk.jmh.annotations.Scope.Benchmark)
class CollectionTest {

  private[this] final val source = (1 to 31310).map(_ => 'a').mkString

  // @Benchmark
  def dec: Int = {
    specializing(0)
  }

  private[this] final def xxx(i: Int): Int = specializing(i)

  @tailrec
  private[this] final def specializing(i: Int): Int = {
    if (i == 31310) {
      0
//    } else if (i % 10000 == 0) {
//      xxx(i + 1)
    } else (source(i): @switch) match {
      case 'b' => specializing(i + 1)
      case 'c' => specializing(i + 1)
      case 'd' => specializing(i + 1)
      case 'e' => specializing(i + 1)
      case 'f' => specializing(i + 1)
      case 'g' => specializing(i + 1)
      case 'h' => specializing(i + 1)
      case 'i' => specializing(i + 1)
      case 'j' => specializing(i + 1)
      case 'k' => specializing(i + 1)
      case 'l' => specializing(i + 1)
      case 'm' => specializing(i + 1)
      case 'n' => specializing(i + 1)
      case 'o' => specializing(i + 1)
      case 'p' => specializing(i + 1)
      case 'q' => specializing(i + 1)
      case 'r' => specializing(i + 1)
      case 's' => specializing(i + 1)
      case 't' => specializing(i + 1)
      case 'u' => specializing(i + 1)
      case 'a' => specializing(i + 1)
    }
    //    @tailrec
    //    def tailing(i: Int): Int = {
    //      if (i < 64) {
    //        sb(i) = 'a'
    //        tailing(i + 1)
    //      } else 64
    //    }
    //    tailing(1)
  }

  dec

}
