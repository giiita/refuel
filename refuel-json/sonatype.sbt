sonatypeProfileName := "com.phylage"

homepage := Some(url("https://github.com/giiita/refuel/tree/master/refuel-json"))

licenses := Seq(
  "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/giiita/refuel"),
    "scm:git@github.com:giiita/refuel.git"
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