import sbt.Keys.crossScalaVersions
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val buildTargetVersion = Seq("2.11.12", "2.12.8", "2.13.0")

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
  crossScalaVersions := buildTargetVersion,
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
  libraryDependencies ++= {
    Seq(
      "org.slf4j" % "slf4j-simple" % "1.7.25" % Provided,

      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    ) ++ {
      scalaVersion.value match {
        case "2.13.0" => Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0")
        case _ => Nil
      }
    }
  }
)

lazy val root = project.in(file("."))
  .aggregate(
    scaladiaMacro,
    scaladiaContainer,
    scaladiaLang,
    scaladiaHttp
  ).settings(
    publishLocal in ThisProject := {},
    publishArtifact in ThisProject := false,
    scalaVersion := GLOBAL_SCALA_VERSION,
    crossScalaVersions := buildTargetVersion
  )

lazy val scaladiaMacro = (project in file("scaladia-macro"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-macro",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= {
      Seq(
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      )
    },
    scalacOptions += "-language:experimental.macros",
    version in ThisProject := "1.0.0"
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
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
    ),
    scalacOptions in Global += "-language:experimental.macros",
    version in ThisProject := "2.0.0"
  ).enablePlugins(JavaAppPackaging)

lazy val scaladiaLang = (project in file("scaladia-lang"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .dependsOn(scaladiaContainer)
  .settings(
    name := "scaladia-lang",
    parallelExecution in Test := true,
    version in ThisProject := "1.0.0"
  ).enablePlugins(JavaAppPackaging)

lazy val scaladiaHttp = (project in file("scaladia-http"))
  .dependsOn(scaladiaLang)
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
    ),
    version in ThisProject := "0.1.0"
  ).enablePlugins(JavaAppPackaging)

val GLOBAL_SCALA_VERSION = "2.13.0"