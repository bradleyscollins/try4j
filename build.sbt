name := "try4j"

organization := "bradleyscollins"

description := "Functional exception handling for Java 8"


javacOptions := Seq(
  "-source", "1.8",
  "-target", "1.8",
  "-Xlint:unchecked",
  "-encoding", "UTF-8"
)

// Javadoc doesn't know about those options
javacOptions in (Compile, doc) := Seq()

// Do not append Scala versions to the generated artifacts
crossPaths := false

// This forbids including Scala related libraries into the dependency
autoScalaLibrary := false


libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.3",
  "com.novocode" % "junit-interface" % "0.10" % "test"
)
