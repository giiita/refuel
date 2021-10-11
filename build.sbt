import sbt.Keys.crossScalaVersions
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val Scala2_13 = "2.13.6"
lazy val Scala3xx  = "3.0.2"

scalaVersion in Scope.Global := Scala2_13
releaseCrossBuild in Scope.Global := true
crossScalaVersions in Scope.Global := Seq(Scala2_13)

val isScala3 = Def.setting(
  CrossVersion.partialVersion(scalaVersion.value).exists(_._1 == 3)
)

lazy val akkaVersion           = "2.6.13"
lazy val akkaHttpVersion       = "10.2.4"
lazy val pac4jVersion          = "4.1.0"
lazy val pureconfigVersion     = "0.14.0"
lazy val scalacheckVersion     = "1.15.2"
lazy val scalamockVersion      = "5.1.0"
lazy val scalaLoggingVersion   = "3.9.2"
lazy val typesafeConfigVersion = "1.4.1"

lazy val scala3PartialBuild = Def.settings(
  crossScalaVersions := Seq(Scala2_13, Scala3xx),
  libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.2.9" % Test) ++ {
      CrossVersion
        .partialVersion(scalaVersion.value) match {
        case Some((2, y)) if y >= 13 =>
          Seq(
            "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"
          )
        case _ => Nil
      }
    }
)

val noPublish = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {},
  // Explicitely skip the doc task because protobuf related Java files causes no type found error
  Compile / doc / sources := Seq.empty,
  Compile / packageDoc / publishArtifact := false
)

lazy val asemble = Seq(
  ThisProject / sonatypeBundleDirectory := (ThisProject / baseDirectory).value / target.value.getName / "sonatype-staging" / s"${version.value}",
  ThisProject / publishTo := sonatypePublishToBundle.value,
  organization := "com.phylage",
  Test / scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-feature",
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-language:higherKinds",
//      "-Ymacro-debug-verbose",
      "-language:implicitConversions"
    ),
  licenses += ("Apache-2.0", url(
      "https://www.apache.org/licenses/LICENSE-2.0.html"
    )),
  releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      ReleaseStep(
        action = Command.process("sonatypeBundleRelease", _),
        enableCrossBuild = true
      )
    )
)

lazy val root = project
  .in(file("."))
  .settings(noPublish)
  .settings(
    ThisProject / releaseProcess := Nil,
    ThisProject / publishLocal := {},
    ThisProject / publishArtifact := false,
    Compile / resourceDirectories += {
      (ThisProject / baseDirectory).value / "project" / "resources"
    }
  )
  .aggregate(
    `containerMacro`,
    container,
    util,
    jsonMacro,
    json,
    //    http,
    //    auth,
    //    cipher,
    //    oauth,
    //    root_interfaces,
    //    interfaces_impl,
    //    call_interfaces
  )
lazy val `containerMacro` = (project in file("refuel-container-macro"))
  .settings(asemble, scala3PartialBuild)
  .settings(
    name := "refuel-container-macro",
    description := "Lightweight DI container Macro for Scala3.",
    Def.settings(
      libraryDependencies ++= {
        if (isScala3.value) {
          Seq(
            "com.typesafe"   % "config"           % typesafeConfigVersion,
            "org.scala-lang" %% "scala3-compiler" % scalaVersion.value,
            "org.scala-lang" %% "scala3-staging"  % scalaVersion.value
          )
        } else {
          Seq(
            "org.scala-lang" % "scala-reflect" % scalaVersion.value,
            "com.typesafe"   % "config"        % typesafeConfigVersion
          )
        }
      },
      scalacOptions ++= {
        if (!isScala3.value) {
          Seq("-language:experimental.macros")
        } else Nil
      }
    )
  )
lazy val `jsonMacro` = (project in file("refuel-json-macro"))
  .dependsOn(container)
  .settings(asemble, scala3PartialBuild)
  .settings(
    name := "refuel-json-macro",
    description := "Refuel Json Macro for Scala3.",
    Def.settings(
      libraryDependencies ++= {
        if (isScala3.value) {
          Seq(
            "com.typesafe"   % "config"           % typesafeConfigVersion,
            "org.scala-lang" %% "scala3-compiler" % scalaVersion.value,
            "org.scala-lang" %% "scala3-staging"  % scalaVersion.value
          )
        } else {
          Seq(
            "org.scala-lang" % "scala-reflect" % scalaVersion.value,
            "com.typesafe"   % "config"        % typesafeConfigVersion
          )
        }
      },
      scalacOptions ++= {
        if (!isScala3.value) {
          Seq("-language:experimental.macros")
        } else Nil
      }
    )
  )
lazy val container = (project in file("refuel-container"))
  .dependsOn(`containerMacro`)
  .settings(asemble, scala3PartialBuild)
  .settings(
    name := "refuel-container",
    description := "Lightweight DI container for Scala.",
    Test / parallelExecution := false,
    Def.settings(
      libraryDependencies ++= {
        if (isScala3.value) {
          Seq(
            "org.scala-lang" %% "scala3-staging" % scalaVersion.value
          )
        } else {
          Seq(
            "org.scala-lang"             % "scala-reflect"  % scalaVersion.value,
            "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
          )
        }
      },
      Global / scalacOptions ++= {
        if (!isScala3.value) {
          Seq(
            "-language:experimental.macros"
          )
        } else Nil
      }
    ),
    Compile / unmanagedClasspath ++= (Compile / unmanagedResources).value
  )
lazy val util = (project in file("refuel-util"))
  .dependsOn(container)
  .settings(asemble, scala3PartialBuild)
  .settings(
    name := "refuel-util"
  )
lazy val json = (project in file("refuel-json"))
  .dependsOn(jsonMacro, util)
  .settings(asemble, scala3PartialBuild)
  .settings(
    name := "refuel-json",
    description := "Various classes serializer / deserializer",
    scalacOptions ++= Seq(
      "-Xlog-implicits"
    ),
    Jmh / resourceDirectory := (Compile / resourceDirectory).value,
    Compile / javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
  )
//  .enablePlugins(JavaAppPackaging, JmhPlugin)
//lazy val cipher = (project in file("refuel-cipher"))
//  .dependsOn(json)
//  .settings(asemble)
//  .settings(
//    name := "refuel-cipher",
//    description := "Cipher module for Scala.",
//    Test / unmanagedClasspath ++= (Compile / unmanagedResources).value
//  )
//  .enablePlugins(JavaAppPackaging)
//lazy val http = (project in file("refuel-http"))
//  .dependsOn(json)
//  .settings(asemble)
//  .settings(
//    Test / aggregate := false,
//    name := "refuel-http",
//    description := "Http client for Scala.",
//    libraryDependencies ++= Seq(
//        "com.typesafe.akka" %% "akka-stream" % akkaVersion     % Provided,
//        "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion % Provided
//      ),
//    Test / unmanagedClasspath ++= (Compile / unmanagedResources).value,
//    Test / testOptions ++= Seq(
//        Tests.Setup { _ =>
//          import scala.sys.process._
//
//          Process("sh sh/setup-testing-http-server.sh").run
//
//          Http.connect("http://localhost:3289/success")
//        },
//        Tests.Cleanup { _ =>
//          import scala.sys.process._
//          println(s"Shutdown exit ${Process("sh sh/shutdown-testing-http-server.sh").!}")
//          println("Shutdown completed.")
//        }
//      )
//  )
//  .enablePlugins(JavaAppPackaging)
//lazy val auth = (project in file("refuel-auth-provider"))
//  .dependsOn(http)
//  .settings(asemble)
//  .settings(
//    ThisBuild / resolvers += ("Shibboleth Repository" at "https://build.shibboleth.net/nexus/content/repositories/releases/"),
//    name := "refuel-auth-provider",
//    description := "Auth provider.",
//    libraryDependencies ++= Seq(
//        "com.typesafe.akka"     %% "akka-stream"         % akkaVersion,
//        "com.typesafe.akka"     %% "akka-http"           % akkaHttpVersion,
//        "org.pac4j"             % "pac4j-saml"           % pac4jVersion,
//        "com.github.pureconfig" %% "pureconfig"          % pureconfigVersion,
//        "org.scalacheck"        %% "scalacheck"          % scalacheckVersion % Test,
//        "com.typesafe.akka"     %% "akka-http-testkit"   % akkaHttpVersion % Test,
//        "com.typesafe.akka"     %% "akka-stream-testkit" % akkaVersion % Test
//      )
//  )
//lazy val oauth = (project in file("refuel-oauth-provider"))
//  .dependsOn(cipher)
//  .settings(asemble)
//  .settings(
//    name := "refuel-oauth-provider",
//    description := "OAuth 2.0 provider.",
//    libraryDependencies ++= Seq(
//        "com.typesafe.akka"     %% "akka-stream"         % akkaVersion,
//        "com.typesafe.akka"     %% "akka-http"           % akkaHttpVersion,
//        "com.github.pureconfig" %% "pureconfig"          % pureconfigVersion,
//        "org.scalacheck"        %% "scalacheck"          % scalacheckVersion % Test,
//        "org.scalamock"         %% "scalamock"           % scalamockVersion % Test,
//        "com.typesafe.akka"     %% "akka-http-testkit"   % akkaHttpVersion % Test,
//        "com.typesafe.akka"     %% "akka-stream-testkit" % akkaVersion % Test
//      )
//  )
//lazy val `test` = (project in file("refuel-test"))
//  .dependsOn(json)
//  .settings(name := "refuel-test", description := "DI testing framework.")
//  .enablePlugins(JavaAppPackaging)
//lazy val root_interfaces =
//  (project in file("test-across-module/root_interfaces"))
//    .dependsOn(http)
//    .settings(
//      Keys.`package` := file(""),
//      ThisProject / publishArtifact := false,
//      ThisProject / releaseProcess := Nil,
//      ThisProject / publish := {},
//      ThisProject / publishLocal := {},
//      ThisProject / publishTo := Some(Opts.resolver.mavenLocalFile)
//    )
//    .enablePlugins(JmhPlugin)
//lazy val interfaces_impl =
//  (project in file("test-across-module/interfaces_impl"))
//    .dependsOn(root_interfaces)
//    .settings(
//      Keys.`package` := file(""),
//      ThisProject / publishArtifact := false,
//      ThisProject / releaseProcess := Nil,
//      ThisProject / publish := {},
//      ThisProject / publishLocal := {},
//      ThisProject / publishTo := Some(Opts.resolver.mavenLocalFile)
//    )
//lazy val call_interfaces =
//  (project in file("test-across-module/call_interfaces"))
//    .dependsOn(interfaces_impl)
//    .settings(
//      Keys.`package` := file(""),
//      ThisProject / publishArtifact := false,
//      ThisProject / releaseProcess := Nil,
//      ThisProject / publish := {},
//      ThisProject / publishLocal := {},
//      ThisProject / publishTo := Some(Opts.resolver.mavenLocalFile)
//    )
