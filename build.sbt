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
    ReleaseStep(action = Command.process("sonatypeRelease", _), enableCrossBuild = true)
  )
)

//lazy val sc213 = scalaVersion.map {
//  case "2.13.1" => {
//    Seq(
//      libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
//      scalacOptions ++= Seq("-Xmax-classfile-name", "256")
//    )
//  }
//  case _ => Nil
//}

def scl213[T](f: => Seq[T]): Def.Initialize[Seq[T]] = Def.setting {
  scalaVersion.value match {
    case "2.13.1" => f
    case _        => Nil
  }
}

def notScl213[T](f: => Seq[T]): Def.Initialize[Seq[T]] = Def.setting {
  scalaVersion.value match {
    case "2.13.1" => Nil
    case _        => f
  }
}

lazy val commonDependencySettings = Seq(
  
  libraryDependencies ++= {
    Seq(
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
<<<<<<< HEAD
<<<<<<< HEAD
    ) ++ {
      scalaVersion.value match {
        case "2.13.0" => Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0")
        case _ => Nil
      }
    }
  }
=======
    ) ++ scl213(scalaVersion.value, Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"))
  },
  scalacOptions ++= scl213(scalaVersion.value, Seq("--Xmax-classfile-name", "200"))
>>>>>>> f9eb751... a
=======
    )
  },
  libraryDependencies ++= scl213(Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0")).value
>>>>>>> db87346... a
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
  scalaVersion := GLOBAL_SCALA_VERSION,
  crossScalaVersions := buildTargetVersion,
  resourceDirectories in Compile += {
    (ThisProject / baseDirectory).value / "project" / "resources"
  }
)

lazy val `macro` = (project in file("scaladia-macro"))
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "scaladia-macro",
    description := "Lightweight DI container for Scala.",
    libraryDependencies ++= {
      Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "com.typesafe" % "config" % "1.3.4"
      )
    },
    scalacOptions += "-language:experimental.macros"
  )

lazy val container = (project in file("scaladia-container"))
  .settings(assemblySettings, commonDependencySettings)
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

lazy val util = (project in file("scaladia-util"))
  .settings(assemblySettings, commonDependencySettings)
  .dependsOn(container)
  .settings(
    name := "scaladia-util",
    parallelExecution in Test := true
  ).enablePlugins(JavaAppPackaging)

lazy val json = (project in file("scaladia-json"))
  .settings(assemblySettings, commonDependencySettings)
  .dependsOn(util)
  .settings(
    name := "scaladia-json",
    description := "Various classes serializer / deserializer",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.7.4"
    ),
    resourceDirectory in Jmh := (resourceDirectory in Compile).value,
    javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions in Compile ++= notScl213(Seq("-Xmax-classfile-name", "255")).value
  ).enablePlugins(JavaAppPackaging, JmhPlugin)

lazy val cipher = (project in file("scaladia-cipher"))
  .dependsOn(json)
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "scaladia-cipher",
    description := "Cipher module for Scala.",
    unmanagedClasspath in Test ++= (unmanagedResources in Compile).value,
  ).enablePlugins(JavaAppPackaging)

lazy val http = (project in file("scaladia-http"))
  .dependsOn(json)
  .settings(assemblySettings, commonDependencySettings)
  .settings(
    name := "scaladia-http",
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

lazy val `test` = (project in file("scaladia-test"))
  .dependsOn(json)
  .settings(assemblySettings, commonDependencySettings)
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
    publishArtifact := false,
    releaseProcess := Nil
  )

lazy val interfaces_impl = (project in file("test-across-module/interfaces_impl"))
  .dependsOn(root_interfaces)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    publishArtifact := false,
    releaseProcess := Nil
  )

lazy val call_interfaces = (project in file("test-across-module/call_interfaces"))
  .dependsOn(interfaces_impl)
  .settings(commonDependencySettings, assemblySettings)
  .settings(
    publishArtifact := false,
    releaseProcess := Nil
  )

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
val GLOBAL_SCALA_VERSION = buildTargetVersion.last
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion(1)
>>>>>>> f472156... REBASE 8 LazyList was only 2.13...
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion.last
>>>>>>> 5bf57dc... REBASE 10
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion.head
>>>>>>> 2028a13... a
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion.last
>>>>>>> 65cd8d0... a
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion(1)
>>>>>>> 9276f54... aa
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion.last
>>>>>>> a04020f... a
=======
val GLOBAL_SCALA_VERSION = buildTargetVersion.last
>>>>>>> 17c1351... change sbt 1.2.8
