defaultSettings

libraryDependencies ++= Seq(
  finagle("core"),
  util("core"),
  twitterServer
) ++ scalaTestDeps
