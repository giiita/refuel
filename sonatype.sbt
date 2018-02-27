useGpg := true
pgpReadOnly := false

sonatypeProfileName := "com.github.giiita"

licenses := Seq(
  "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/giiita/scaladia"),
    "scm:git@github.com:giiita/scaladia.git"
  )
)
developers := List(
  Developer(
    id="giiita",
    name="Giita",
    email="omarun_inori@yahoo.co.jp",
    url=url("https://github.com/giiita")
  )
)

publishMavenStyle := true

pomExtra in Global := {
  <url>https://github.com/giiita/scaladia</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com:giiita/scaladia.git</connection>
      <developerConnection>scm:git:git@github.com:giiita/scaladia.git</developerConnection>
      <url>https://github.com/giiita/scaladia</url>
    </scm>
    <developers>
      <developer>
        <id>giiita</id>
        <name>Giita</name>
        <email>omarun_inori@yahoo.co.jp</email>
        <url>https://github.com/giiita</url>
      </developer>
    </developers>
}