import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "release"
  val appVersion      = "1.0-SNAPSHOT"

  val mandubianRepo = Seq(
    "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
    "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/"
  )

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.reactivemongo" %% "reactivemongo" % "0.9",
    "play-autosource"   %% "reactivemongo" % "0.1-SNAPSHOT"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= mandubianRepo     
  )

}
