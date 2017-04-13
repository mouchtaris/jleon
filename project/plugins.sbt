// Sonatype's scalastyle repository
resolvers += "Sonatype scalastyle repository" at "https://oss.sonatype.org/content/repositories/releases/org/scalastyle"
// Typesafe's repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.5")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

// Scalariform
resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")


//
// Section Experimental
//
// scala-native
addSbtPlugin("org.scala-native" % "sbt-scala-native"  % "0.1.0")
// coursier
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")
