sonatypeProfileName := "com.phylage"

homepage := Some(url("https://github.com/giiita/scaladia/tree/master/scaladia-lang"))

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
    email="r.takagi4263@gmail.com",
    url=url("https://github.com/giiita")
  )
)

publishMavenStyle := true