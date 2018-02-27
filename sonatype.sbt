sonatypeProfileName := "com.github.giiita"

homepage := Some(url("https://github.com/giiita/scaladia"))

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

publishMavenStyle := false