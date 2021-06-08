package refuel.container

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.util.Try

private[refuel] object Config {

  trait SkippedPackage {
    val value: String
  }

  case class DefaultPackage(value: String) extends SkippedPackage

  case class AdditionalPackage(value: String) extends SkippedPackage

  implicit class RichPackageString(value: String) {
    def asDefault: SkippedPackage = DefaultPackage(value)

    def asAddition: SkippedPackage = AdditionalPackage(value)
  }

  val blackList: List[SkippedPackage] = Try {
    {
      try {
        ConfigFactory
          .load(getClass.getClassLoader, "di.conf")
          .getStringList("unscanning.package")
          .asScala
          .toList
          .map(_.asAddition)
      } catch { e => List.empty }
    }  ++ List(
          "com.oracle",
          "com.apple",
          "com.sun",
          "com.google",
          "oracle",
          "apple",
          "scala",
          "javafx",
          "javax",
          "sun",
          "java",
          "jdk",
          "<empty>",
          "coursierapi",
          "xsbti",
          "org.graalvm",
          "dotty.tools",
          "org.scalajs",
          "org.xml",
          "org.jline",
          "org.jcp",
          "org.jcp",
          "scala"
        ).map(_.asDefault)
    }.get
}
