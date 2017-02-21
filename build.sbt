name         := "fancy-poi"
organization := "org.fancypoi"
version      := "1.2.1"
scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.4", "2.11.8", "2.12.1")

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "3.15",
  "org.apache.poi" % "poi-ooxml" % "3.15"
)
