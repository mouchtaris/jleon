// Copyright (C) 2015-2017 Red Tulip Systems BV.
//
// Author nikos.mouchtaris@redtulipsystems.com

import sbt._
import Keys._

object ScalacheckConfiguration {

  val ProjectSettings = Seq(

    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )

  )
}
