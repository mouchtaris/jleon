import sbt._
import Keys._

object Experimental {

  lazy val ProjectSettings = Seq(

    libraryDependencies ++= Seq(
//      "com.lihaoyi" % "ammonite" % "0.8.2" % "test" cross CrossVersion.full,
      "com.github.alexarchambault" %% "argonaut-shapeless_6.2" % "1.2.0-M4",
      "com.chuusai" %% "shapeless" % "2.3.2",
      "org.scalaz" %% "scalaz-core" % "7.2.9",

      "com.typesafe.akka" %% "akka-actor" % "2.4.17",
      "com.typesafe.akka" %% "akka-agent" % "2.4.17",
      "com.typesafe.akka" %% "akka-camel" % "2.4.17",
      "com.typesafe.akka" %% "akka-cluster" % "2.4.17",
      "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4.17",
      "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.17",
      "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.17",
      "com.typesafe.akka" %% "akka-contrib" % "2.4.17",
      "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.4.17",
      "com.typesafe.akka" %% "akka-osgi" % "2.4.17",
      "com.typesafe.akka" %% "akka-persistence" % "2.4.17",
      "com.typesafe.akka" %% "akka-persistence-tck" % "2.4.17",
      "com.typesafe.akka" %% "akka-remote" % "2.4.17",
      "com.typesafe.akka" %% "akka-slf4j" % "2.4.17",
      "com.typesafe.akka" %% "akka-stream" % "2.4.17",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.17",
      "com.typesafe.akka" %% "akka-testkit" % "2.4.17",
      "com.typesafe.akka" %% "akka-distributed-data-experimental" % "2.4.17",
      "com.typesafe.akka" %% "akka-typed-experimental" % "2.4.17",
      "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.17",

      "com.typesafe.akka" %% "akka-http-core" % "10.0.4",
      "com.typesafe.akka" %% "akka-http" % "10.0.4",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.4",
      "com.typesafe.akka" %% "akka-http-jackson" % "10.0.4",
      "com.typesafe.akka" %% "akka-http-xml" % "10.0.4"
    )

    //initialCommands in (Test, console) := """ammonite.Main().run()"""
  )
}
