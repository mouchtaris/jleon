lazy val Jleon = (project in file("."))
  .enablePlugins(
//    AshScriptPlugin,
//    DockerPlugin,
//    JavaAppPackaging,
    SbtScalariform
//    ScalaNativePlugin
  )
  .settings(
    organization := "good-vibez",
    name := "jleon",
    version := "0.0.1",
    scalaVersion := "2.12.2"
  )
  .settings(
    // Extra project settings
    ProjectConfiguration.ProjectSettings: _*
  )
  .settings(
    // Scoverage settings
    ScoverageConfiguration.ScoverageSettings: _*
  )
  .settings(
    // Scalariform settings
    ScalariformConfiguration.ScalariformSettings: _*
  )
  .settings(
    // Scaladoc settings
    ScaladocConfiguration.ProjectSettings: _*
  )
  .settings(
    ScalacheckConfiguration.ProjectSettings: _*
  )
  .settings(
    // Telephony providers settings
    TelephonyConfiguration.ProjectSettings: _*
  )
  .settings(
    // Akka settings
    AkkaConfiguration.ProjectSettings: _*
  )
  .settings(
    Experimental.ProjectSettings: _*
  )
  .settings(
    //
    // Docker Plugin Configuration
    //
    packageName in Docker := "jleon",
    dockerBaseImage := "openjdk:8-jre-alpine",
    dockerExposedPorts := Seq(8080),
    dockerUpdateLatest := true,

    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-explaintypes",
      "-feature",
      "-target:jvm-1.8",
      "-unchecked",
      "-Xfuture",
      "-Xlint",
      "-Xprint:type",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    ),

    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "javax.sip" % "jain-sip-ri" % "1.2.324"
    )
  )
