// Copyright (C) 2015-2017 Red Tulip Systems BV.
//
// Author nikos.mouchtaris@redtulipsystems.com

import sbt._
import Keys._

object AkkaConfiguration {

  final val AkkaVersion = "2.4.17"
  final val AkkaHttpVersion = "10.0.5"

  val ProjectSettings = Seq(

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion
    )

  )

}
