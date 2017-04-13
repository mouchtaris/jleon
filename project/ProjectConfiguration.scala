// Copyright (C) 2015-2017 Red Tulip Systems BV.
//
// Author nikos.mouchtaris@redtulipsystems.com

import sbt._
import Keys._

object ProjectConfiguration {

  val ProjectSettings = Seq(
    scalacOptions in (Compile, doc) ++= Seq(
      "-groups",
      "-implicits"
    ),
    autoAPIMappings := true
  )

}
