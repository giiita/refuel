import sbt.Keys.crossScalaVersions
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

releaseCrossBuild in Scope.Global := true
crossScalaVersions in Scope.Global := Seq("2.12.12", "2.13.5")
scalaVersion in Scope.Global := "2.12.12"

lazy val akkaVersion           = "2.6.13"
lazy val akkaHttpVersion       = "10.2.4"
lazy val pac4jVersion          = "4.1.0"
lazy val pureconfigVersion     = "0.14.0"
lazy val scalacheckVersion     = "1.15.2"
lazy val scalamockVersion      = "5.1.0"
lazy val scalaLoggingVersion   = "3.9.2"
lazy val typesafeConfigVersion = "1.4.1"

libraryDependencies in Scope.Global ++= {
  Seq("org.scalatest" %% "scalatest" % "3.2.3" % Test)
}
libraryDependencies in Scope.Global ++= scl213(
  Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0")
).value

lazy val asemble = Seq(
  sonatypeBundleDirectory in ThisProject := (ThisProject / baseDirectory).value / target.value.getName / "sonatype-staging" / s"${version.value}",
  publishTo in ThisProject := sonatypePublishToBundle.value,
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
  .aggregate(
    `macro`,
    container,
    util,
    json,
    http,
    auth,
    cipher,
    oauth,
    root_interfaces,
    interfaces_impl,
    call_interfaces
  )
  .settings(
    releaseProcess in ThisProject := Nil,
    publishLocal in ThisProject := {},
    publishArtifact in ThisProject := false,
    resourceDirectories in Compile += {
      (ThisProject / baseDirectory).value / "project" / "resources"
    }
  )
lazy val `macro` = (project in file("refuel-macro"))
  .settings(asemble)
  .settings(
    name := "refuel-macro",
    description := "Lightweight DI container for Scala.",
    resourceDirectory in Jmh := (resourceDirectory in Compile).value,
    javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= {
      Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "com.typesafe"   % "config"        % typesafeConfigVersion
      )
    },
    scalacOptions += "-language:experimental.macros"
  )
lazy val container = (project in file("refuel-container"))
  .dependsOn(`macro`)
  .settings(asemble)
  .settings(
    name := "refuel-container",
    description := "Lightweight DI container for Scala.",
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
        "org.scala-lang"             % "scala-reflect"  % scalaVersion.value,
        "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
      ),
    scalacOptions in Global ++= Seq(
//            "-Ydebug",
//             "-Ymacro-debug-verbose",
        "-language:experimental.macros"
      ),
    unmanagedClasspath in Compile ++= (unmanagedResources in Compile).value
  )
lazy val util = (project in file("refuel-util"))
  .dependsOn(container)
  .settings(asemble)
  .settings(
    name := "refuel-util"
  )
  .enablePlugins(JavaAppPackaging)
lazy val json = (project in file("refuel-json"))
  .dependsOn(util)
  .settings(asemble)
  .settings(
    name := "refuel-json",
    description := "Various classes serializer / deserializer",
    resourceDirectory in Jmh := (resourceDirectory in Compile).value,
    javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8")
  )
  .enablePlugins(JavaAppPackaging, JmhPlugin)
lazy val cipher = (project in file("refuel-cipher"))
  .dependsOn(json)
  .settings(asemble)
  .settings(
    name := "refuel-cipher",
    description := "Cipher module for Scala.",
    unmanagedClasspath in Test ++= (unmanagedResources in Compile).value
  )
  .enablePlugins(JavaAppPackaging)
lazy val http = (project in file("refuel-http"))
  .dependsOn(json)
  .settings(asemble)
  .settings(
    aggregate in Test := false,
    name := "refuel-http",
    description := "Http client for Scala.",
    libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-stream" % akkaVersion     % Provided,
        "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion % Provided
      ),
    unmanagedClasspath in Test ++= (unmanagedResources in Compile).value,
    testOptions in Test ++= Seq(
        Tests.Setup { _ =>
          import scala.sys.process._

          Process("sh sh/setup-testing-http-server.sh").run

          Http.connect("http://localhost:3289/success")
        },
        Tests.Cleanup { _ =>
          import scala.sys.process._
          println(s"Shutdown exit ${Process("sh sh/shutdown-testing-http-server.sh").!}")
          println("Shutdown completed.")
        }
      )
  )
  .enablePlugins(JavaAppPackaging)
lazy val auth = (project in file("refuel-auth-provider"))
  .dependsOn(http)
  .settings(asemble)
  .settings(
    ThisBuild / resolvers += ("Shibboleth Repository" at "https://build.shibboleth.net/nexus/content/repositories/releases/"),
    name := "refuel-auth-provider",
    description := "Auth provider.",
    libraryDependencies ++= Seq(
        "com.typesafe.akka"     %% "akka-stream"         % akkaVersion,
        "com.typesafe.akka"     %% "akka-http"           % akkaHttpVersion,
        "org.pac4j"             % "pac4j-saml"           % pac4jVersion,
        "com.github.pureconfig" %% "pureconfig"          % pureconfigVersion,
        "org.scalacheck"        %% "scalacheck"          % scalacheckVersion % Test,
        "com.typesafe.akka"     %% "akka-http-testkit"   % akkaHttpVersion % Test,
        "com.typesafe.akka"     %% "akka-stream-testkit" % akkaVersion % Test
      )
  )
lazy val oauth = (project in file("refuel-oauth-provider"))
  .dependsOn(cipher)
  .settings(asemble)
  .settings(
    name := "refuel-oauth-provider",
    description := "OAuth 2.0 provider.",
    libraryDependencies ++= Seq(
        "com.typesafe.akka"     %% "akka-stream"         % akkaVersion,
        "com.typesafe.akka"     %% "akka-http"           % akkaHttpVersion,
        "com.github.pureconfig" %% "pureconfig"          % pureconfigVersion,
        "org.scalacheck"        %% "scalacheck"          % scalacheckVersion % Test,
        "org.scalamock"         %% "scalamock"           % scalamockVersion % Test,
        "com.typesafe.akka"     %% "akka-http-testkit"   % akkaHttpVersion % Test,
        "com.typesafe.akka"     %% "akka-stream-testkit" % akkaVersion % Test
      )
  )
lazy val `test` = (project in file("refuel-test"))
  .dependsOn(json)
  .settings(name := "refuel-test", description := "DI testing framework.")
  .enablePlugins(JavaAppPackaging)
lazy val root_interfaces =
  (project in file("test-across-module/root_interfaces"))
    .dependsOn(http)
    .settings(
      Keys.`package` := file(""),
      publishArtifact in ThisProject := false,
      releaseProcess in ThisProject := Nil,
      publish in ThisProject := {},
      publishLocal in ThisProject := {},
      publishTo in ThisProject := Some(Opts.resolver.mavenLocalFile)
    )
    .enablePlugins(JmhPlugin)
lazy val interfaces_impl =
  (project in file("test-across-module/interfaces_impl"))
    .dependsOn(root_interfaces)
    .settings(
      Keys.`package` := file(""),
      publishArtifact in ThisProject := false,
      releaseProcess in ThisProject := Nil,
      publish in ThisProject := {},
      publishLocal in ThisProject := {},
      publishTo in ThisProject := Some(Opts.resolver.mavenLocalFile)
    )
lazy val call_interfaces =
  (project in file("test-across-module/call_interfaces"))
    .dependsOn(interfaces_impl)
    .settings(
      Keys.`package` := file(""),
      publishArtifact in ThisProject := false,
      releaseProcess in ThisProject := Nil,
      publish in ThisProject := {},
      publishLocal in ThisProject := {},
      publishTo in ThisProject := Some(Opts.resolver.mavenLocalFile)
    )

def scl213[T](f: => Seq[T]): Def.Initialize[Seq[T]] = Def.setting {
  scalaVersion.value match {
    case "2.13.4" => f
    case _        => Nil
  }
}

def notScl213[T](f: => Seq[T]): Def.Initialize[Seq[T]] = Def.setting {
  scalaVersion.value match {
    case "2.13.4" => Nil
    case _        => f
  }
}
