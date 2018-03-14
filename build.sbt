import ReleaseTransformations._

name := "scaladia"

description := "Lightweight DI container for Scala."

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.12.4"

releaseCrossBuild := true

crossScalaVersions := Seq("2.11.12", "2.12.4")

organization := "com.github.giiita"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.4",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

parallelExecution in Test := false

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
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
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
