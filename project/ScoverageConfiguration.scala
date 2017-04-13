// Copyright (C) 2015-2017 Red Tulip Systems BV.
//
// Author nikos.mouchtaris@redtulipsystems.com

import sbt._
import Process._
import Keys._
import scoverage.ScoverageKeys._

object ScoverageConfiguration {
  val ScoverageSettings = Seq(
    coverageExcludedFiles := "" +
      "",
    coverageMinimum := 100,
    coverageFailOnMinimum := true
  )
}
