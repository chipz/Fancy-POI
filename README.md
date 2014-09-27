##Fancy scala wapper for Apache POI

---------------

This fork just ads sbt config, so that the project can be included in other sbt projects. nothing more

###Example for including in other sbt project: 

create a project/Build.scala like this

    import sbt._
    import Keys._

    object MyProjectBuild extends Build {

      val mySettings = Defaults.defaultSettings ++ Seq(
        name := "projectname",
        version := "1.0",
        scalaVersion := "2.10.4",
        libraryDependencies ++= Seq(
           "org.apache.poi" % "poi" % "3.10.1",
           "org.apache.poi" % "poi-ooxml" % "3.10.1"
        )
      )

      lazy val myProject = Project("rootProject", file("."), settings = mySettings) dependsOn(fancyPoy)

      lazy val fancyPoy = RootProject(uri("git://github.com/rompetroll/Fancy-POI.git"))
    }

NOTE: this way of depending on a github project is not very stable.
