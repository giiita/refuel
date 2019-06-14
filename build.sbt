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
  crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.0"),
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  assemblyMergeStrategy in assembly := {
    case PathList(ps@_*) if ps.last endsWith ".class"      => MergeStrategy.first
    case PathList(ps@_*) if ps.last endsWith "BUILD"       => MergeStrategy.first
    case PathList(ps@_*) if ps.last endsWith ".properties" => MergeStrategy.first
    case ps                                                => (assemblyMergeStrategy in assembly).value(ps)
  },
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    // setReleaseVersion,
    // commitReleaseVersion,
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
    scaladiaContainerCore,
    scaladiaHttp,
    all
  ).settings(
    publishLocal in ThisProject := {},
    publishArtifact in ThisProject := false,
    scalaVersion := GLOBAL_SCALA_VERSION,
    crossScalaVersions := Seq("2.11.12", "2.12.8")
  )

lazy val all = (project in file("scaladia-all"))
  .dependsOn(
    scaladiaContainerCore,
    scaladiaHttp
  )
  .settings(assemblySettings)
  .settings(
    name := "scaladia",
    description := "Scaladia all libraries.",
    version in ThisProject := "1.6.4"
  )

lazy val scaladiaContainerCore = (project in file("scaladia-container-core"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-container-core",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.2",
      "org.scala-lang" % "scala-reflect" % "2.13.0",
      "org.slf4j" % "slf4j-api" % "1.7.25"
    ),
    version in ThisProject := "1.5.8"
  )

lazy val scaladiaHttp = (project in file("scaladia-http"))
  .dependsOn(scaladiaContainerCore)
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-http",
    description := "Http client for Scala.",
    libraryDependencies ++= Seq(
      "org.dispatchhttp" % "dispatch-core_2.12" % "0.14.0",
      "com.twitter" % "finatra-http_2.12" % "19.5.1"
    ),
    version in ThisProject := "0.0.6"
  )

val GLOBAL_SCALA_VERSION = "2.13.0"