name := "fancy-poi"

organization := "org.fancypoi"

version := "1.1"

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "3.14",
  "org.apache.poi" % "poi-ooxml" % "3.14"
)

scalaVersion := "2.10.4"
