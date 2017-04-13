// Copyright (C) 2015-2017 Red Tulip Systems BV.
//
// Author nikos.mouchtaris@redtulipsystems.com

import sbt._
import Process._
import Keys._


object TelephonyConfiguration {

  val ProjectSettings = Seq(

    resolvers += Resolver.sonatypeRepo("snapshots"),

    libraryDependencies ++= Seq(
      "com.twilio.sdk" % "twilio" % "7.5.0",
      "com.nexmo" % "client" % "2.0.0-SNAPSHOT",
      "commons-net" % "commons-net" % "3.5"
    )
  )

}
