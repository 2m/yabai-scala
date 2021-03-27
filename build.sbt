scalaVersion := "3.0.0-RC2"

scalacOptions += "-source:future"

libraryDependencies ++= Seq(
  ("io.circe"     %% "circe-parser" % "0.13.0").cross(CrossVersion.for3Use2_13),
  ("io.circe"     %% "circe-optics" % "0.13.0").cross(CrossVersion.for3Use2_13),
  "org.scalameta" %% "munit"        % "0.7.23" % Test
)

testFrameworks += new TestFramework("munit.Framework")
