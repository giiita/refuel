import sbt.Keys.crossScalaVersions
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val buildTargetVersion = Seq("2.11.12", "2.12.10", "2.13.1")
scalaVersion in ThisBuild := "2.13.1"

publishTo in ThisBuild := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

lazy val assemblySettings = Seq(
  organization := "com.phylage",
  scalacOptions in Test ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-language:higherKinds",
    "-language:implicitConversions"
  ),
  releaseCrossBuild := true,
  crossScalaVersions := buildTargetVersion,
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    ReleaseStep(action = Command.process("sonatypeBundleRelease", _), enableCrossBuild = true)
  )
)

def scl213[T](f: => Seq[T]): Def.Initialize[Seq[T]] = Def.setting {
  scalaVersion.value match {
    case "2.13.1" => f
    case _ => Nil
  }
}

def notScl213[T](f: => Seq[T]): Def.Initialize[Seq[T]] = Def.setting {
  scalaVersion.value match {
    case "2.13.1" => Nil
    case _ => f
  }
}

lazy val commonDependencySettings = Seq(

  libraryDependencies ++= {
    Seq(
      "org.scalatest" %% "scalatest" % "3.1.0" % Test
    )
  },
  libraryDependencies ++= scl213(Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0")).value
)

lazy val root = project.in(file("."))
  .aggregate(
    `macro`,
    container,
    util,
    json,
    http,
    root_interfaces,
    interfaces_impl,
    call_interfaces
  ).settings(
  publishLocal in ThisProject := {},
  publishArtifact in ThisProject := false,
  crossScalaVersions := buildTargetVersion,
  resourceDirectories in Compile += {
    (ThisProject / baseDirectory).value / "project" / "resources"
  }
)

lazy val `macro` = (project in file("refuel-macro"))
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "refuel-macro",
    description := "Lightweight DI container for Scala.",
    libraryDependencies ++= {
      Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "com.typesafe" % "config" % "1.3.4"
      )
    },
    scalacOptions += "-language:experimental.macros"
  )

lazy val container = (project in file("refuel-container"))
  .settings(assemblySettings, commonDependencySettings)
  .dependsOn(`macro`)
  .settings(
    name := "refuel-container",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
    ),
    scalacOptions in Global ++= Seq(
      //      "-Ydebug",
      //       "-Ymacro-debug-verbose",
      "-language:experimental.macros"
    ),
    unmanagedClasspath in Compile ++= (unmanagedResources in Compile).value
  ).enablePlugins(JavaAppPackaging)

lazy val util = (project in file("refuel-util"))
  .settings(assemblySettings, commonDependencySettings)
  .dependsOn(container)
  .settings(
    name := "refuel-util",
    parallelExecution in Test := true
  ).enablePlugins(JavaAppPackaging)

lazy val json = (project in file("refuel-json"))
  .settings(assemblySettings, commonDependencySettings)
  .dependsOn(util)
  .settings(
    name := "refuel-json",
    description := "Various classes serializer / deserializer",
    resourceDirectory in Jmh := (resourceDirectory in Compile).value,
    javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.7.4"
    )
  ).enablePlugins(JavaAppPackaging, JmhPlugin)

lazy val cipher = (project in file("refuel-cipher"))
  .dependsOn(json)
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "refuel-cipher",
    description := "Cipher module for Scala.",
    unmanagedClasspath in Test ++= (unmanagedResources in Compile).value,
  ).enablePlugins(JavaAppPackaging)

lazy val http = (project in file("refuel-http"))
  .dependsOn(json)
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "refuel-http",
    description := "Http client for Scala.",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.23",
      "com.typesafe.akka" %% "akka-stream" % "2.5.23",
      "com.typesafe.akka" %% "akka-http-jackson" % "10.1.8",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"
    ),
    unmanagedClasspath in Test ++= (unmanagedResources in Compile).value,
    testOptions in Test ++= Seq(
      Tests.Setup { _ =>
        import scala.sys.process._

        Process("sh sh/setup-testing-http-server.sh").run

        Http.connect("http://localhost:3289/endpoint")
      },
      Tests.Cleanup { _ =>
        import scala.sys.process._
        Process("sh sh/shutdown-testing-http-server.sh").run
      }
    )
  ).enablePlugins(JavaAppPackaging)

lazy val `test` = (project in file("refuel-test"))
  .dependsOn(json)
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "refuel-test",
    description := "DI testing framework."
  ).enablePlugins(JavaAppPackaging)

lazy val root_interfaces = (project in file("test-across-module/root_interfaces"))
  .dependsOn(http)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    publishArtifact in ThisProject := false,
    releaseProcess in ThisProject := Nil,
    publish in ThisProject := {},
    publishLocal in ThisProject := {},
    publishTo in ThisProject := Some(Opts.resolver.mavenLocalFile)
  ).enablePlugins(JmhPlugin)

lazy val interfaces_impl = (project in file("test-across-module/interfaces_impl"))
  .dependsOn(root_interfaces)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    publishArtifact in ThisProject := false,
    releaseProcess in ThisProject := Nil,
    publish in ThisProject := {},
    publishLocal in ThisProject := {},
    publishTo in ThisProject := Some(Opts.resolver.mavenLocalFile)
  )

lazy val call_interfaces = (project in file("test-across-module/call_interfaces"))
  .dependsOn(interfaces_impl)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    publishArtifact in ThisProject := false,
    releaseProcess in ThisProject := Nil,
    publish in ThisProject := {},
    publishLocal in ThisProject := {},
    publishTo in ThisProject := Some(Opts.resolver.mavenLocalFile)
  )
