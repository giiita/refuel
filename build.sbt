import ReleaseTransformations._

name := "scaladia"

version := "0.1"

description := "Lightweight DI container for Scala."

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

crossScalaVersions := Seq("2.11.12", "2.12.4")

organization := "com.github.giiita"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.4",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

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
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeRelease", _)),
  pushChanges
)
