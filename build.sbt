name := "Try4J"

organization := "try4j"

description := "Functional exception handling for Java 8"

homepage := Some(url("https://github.com/bradleyscollins/try4j"))

licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))


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
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

// Bintray
seq(bintraySettings:_*)

// sbt-release
releaseSettings

// sbt-github-release
GithubRelease.defaults

GithubRelease.repo := "bradleyscollins/try4j"

GithubRelease.draft := true