addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")


resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases")
)(Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")


addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")


resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

addSbtPlugin("ohnosequences" % "sbt-github-release" % "0.1.2")
