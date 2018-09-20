import sbt.Keys.crossScalaVersions
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val assemblySettings = Seq(
  scalaVersion := GLOBAL_SCALA_VERSION,
  assemblyJarName in assembly := {
    s"${name.value}-${version.value}.jar"
  },
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  ),
  organization := "com.github.giiita",
  scalacOptions in Test ++= Seq("-Yrangepos", "-Xlint", "-deprecation", "-unchecked", "-feature"),
  releaseCrossBuild := true,
  crossScalaVersions := Seq("2.11.12", "2.12.4"),
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeRelease", _), enableCrossBuild = true)
)
lazy val root = project.in(file("."))
  .aggregate(
    injectorCore
  )
  .dependsOn(injectorCore)
  .settings(assemblySettings)
  .settings(
    name := "scaladia",
    description := "Scaladia all libraries."
  )

lazy val injectorCore = (project in file("scaladia-container-core"))
  .settings(assemblySettings)
  .settings(
    name := "scaladia-container-core",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.4",
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    )
  )

val GLOBAL_SCALA_VERSION = "2.12.4"