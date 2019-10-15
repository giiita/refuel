addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.7")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.2-1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.22")

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "3.0.3"
)
