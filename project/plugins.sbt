addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.0")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.22")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.3.2")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "3.0.3"
)
