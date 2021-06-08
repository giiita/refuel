sonatypeProfileName := "com.phylage"

licenses := Seq(
  "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)

(Global / pomExtra) := {
  <url>https://github.com/giiita/refuel</url>
    <scm>
      <connection>scm:git:github.com/giiita/refuel.git</connection>
      <developerConnection>scm:git:git@github.com:giiita/refuel.git</developerConnection>
      <url>github.com/giiita/refuel.git</url>
    </scm>
    <developers>
      <developer>
        <id>giiita</id>
        <name>Giiita</name>
        <url>https://github.com/giiita</url>
      </developer>
    </developers>
}

publishMavenStyle := true
