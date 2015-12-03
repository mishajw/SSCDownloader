name := "SSCDownloaderLift"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

seq(webSettings: _*)

libraryDependencies ++= {
  val liftVersion = "2.6-RC1"
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" %
      "container,compile" artifacts Artifact("javax.servlet", "jar", "jar"),
//    "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.15",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.slf4j" % "slf4j-simple" % "1.7.5",
    "org.clapper" %% "grizzled-slf4j" % "1.0.2",
    "com.typesafe.akka" %% "akka-actor" % "2.3.11",
    "org.jsoup" % "jsoup" % "1.8.3"
  )
}