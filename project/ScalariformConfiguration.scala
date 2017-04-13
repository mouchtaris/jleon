// Copyright (C) 2015-2017 Red Tulip Systems BV.
//
// Author nikos.mouchtaris@redtulipsystems.com

import sbt._
import Process._
import Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

//
// Settings for Scalariform:
// * https://github.com/scala-ide/scalariform#preferences
// * https://github.com/sbt/sbt-scalariform#advanced-configuration
//
// The documentation on Scalafirm main repo is more recent than
// the published artifacts.
//
// Options which are commented out are introduced on the 'master'
// branch of scalariform but are not published on Maven Repos.
//
object ScalariformConfiguration {
  val ScalariformSettings =
    SbtScalariform.scalariformSettings ++ Seq(
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
        .setPreference(AlignParameters, true)
        // .setPreference(FirstParameterOnNewline, "Force")
        .setPreference(AlignArguments, true)
        // .setPreference(FirstArgumentOnNewline, "Force")
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(CompactControlReadability, true)
        .setPreference(CompactStringConcatenation, false)
        .setPreference(DoubleIndentClassDeclaration, true)
        // .setPreference(DoubleIndentMethodDeclaration, true)
        .setPreference(IndentPackageBlocks, true)
        .setPreference(IndentSpaces, 2)
        .setPreference(IndentWithTabs, false)
        .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
        // .setPreference(NewlineAtEndOfFile, true)
        .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
        .setPreference(PreserveSpaceBeforeArguments, false)
        //.setPreference(DanglingCloseParenthesis, "Force")
        .setPreference(RewriteArrowSymbols, true)
        // .setPreference(SpaceBeforColon, false)
        // .setPreference(SpaceBeforeContextColon, true)
        .setPreference(SpaceInsideBrackets, false)
        .setPreference(SpaceInsideParentheses, false)
        .setPreference(SpacesWithinPatternBinders, true)
        // .setPreference(SoacesAroundMultiImports, true)
      )
}
