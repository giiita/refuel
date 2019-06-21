import sbt.Keys.crossScalaVersions
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val assemblySettings = Seq(
  scalaVersion := GLOBAL_SCALA_VERSION,
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  ),
  organization := "com.phylage",
  scalacOptions in Test ++= Seq("-Yrangepos", "-Xlint", "-deprecation", "-unchecked", "-feature"),
  releaseCrossBuild := true,
  crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.0"),
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    ReleaseStep(action = Command.process("sonatypeRelease", _), enableCrossBuild = true)
  )
)

lazy val commonDependencySettings = Seq(
  excludeDependencies ++= Seq(
    ExclusionRule("org.scala-lang.modules", "scala-xml_2.12")
  ),
  libraryDependencies ++= Seq(
    // scala parallel collection
    "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",

    "org.slf4j" % "slf4j-simple" % "1.7.25" % Provided,
    
    "org.scalatest" %% "scalatest" % "3.0.8" % Test
  )
)

lazy val root = project.in(file("."))
  .aggregate(
    scaladiaContainer,
    scaladiaHttp
  ).settings(
    publishLocal in ThisProject := {},
    publishArtifact in ThisProject := false,
    scalaVersion := GLOBAL_SCALA_VERSION,
    crossScalaVersions := Seq("2.11.12", "2.12.8")
  )

lazy val scaladiaMacro = (project in file("scaladia-macro"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-macro",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.2",
      "org.scala-lang" % "scala-reflect" % "2.13.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.softwaremill.macwire" %% "macros" % "2.3.3"
    ),
    scalacOptions in Global += "-language:experimental.macros"
  )

lazy val scaladiaContainer = (project in file("scaladia-container"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .dependsOn(scaladiaMacro)
  .settings(
    name := "scaladia-container",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.2",
      "org.scala-lang" % "scala-reflect" % "2.13.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.softwaremill.macwire" %% "macros" % "2.3.3"
    ),
    scalacOptions in Global += "-language:experimental.macros",
    version in ThisProject := "0.0.1"
  ).enablePlugins(JavaAppPackaging)

lazy val scaladiaHttp = (project in file("scaladia-http"))
  .dependsOn(scaladiaContainer)
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-http",
    description := "Http client for Scala.",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.1.8",
      "com.typesafe.akka" %% "akka-actor" % "2.5.23",
      "com.typesafe.akka" %% "akka-stream" % "2.5.23",
      "com.typesafe.akka" %% "akka-http-jackson" % "10.1.8",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"
      // "com.twitter" % "finatra-http_2.12" % "19.5.1"
    ),
    version in ThisProject := "0.0.6"
  )

val GLOBAL_SCALA_VERSION = "2.13.0"