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
    `macro`,
    container,
    lang,
    http,
    root_interfaces,
    interfaces_impl,
    call_interfaces
  ).settings(
  publishLocal in ThisProject := {},
  publishArtifact in ThisProject := false,
  scalaVersion := GLOBAL_SCALA_VERSION,
  crossScalaVersions := buildTargetVersion
)

lazy val `macro` = (project in file("scaladia-macro"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-macro",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= {
      Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "com.typesafe" % "config" % "1.3.4"
      )
    },
    scalacOptions += "-language:experimental.macros"
  )

lazy val container = (project in file("scaladia-container"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .dependsOn(`macro`)
  .settings(
    name := "scaladia-container",
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

lazy val lang = (project in file("scaladia-lang"))
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .dependsOn(container)
  .settings(
    name := "scaladia-lang",
    parallelExecution in Test := true
  ).enablePlugins(JavaAppPackaging)

lazy val http = (project in file("scaladia-http"))
  .dependsOn(lang)
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
    )
  ).enablePlugins(JavaAppPackaging)

lazy val `test` = (project in file("scaladia-test"))
  .dependsOn(http)
  .settings(assemblySettings)
  .settings(commonDependencySettings)
  .settings(
    name := "scaladia-test",
    description := "DI testing framework.",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.8"
    )
  ).enablePlugins(JavaAppPackaging)

lazy val root_interfaces = (project in file("test-across-module/root_interfaces"))
  .dependsOn(http)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    releaseProcess := Nil
  )

lazy val interfaces_impl = (project in file("test-across-module/interfaces_impl"))
  .dependsOn(root_interfaces)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    releaseProcess := Nil
  )

lazy val call_interfaces = (project in file("test-across-module/call_interfaces"))
  .dependsOn(interfaces_impl)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    releaseProcess := Nil
  )

val GLOBAL_SCALA_VERSION = "2.12.8"