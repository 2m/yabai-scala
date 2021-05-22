organization := "lt.dvim.yabai"
name := "yabai-scala"
description := "Scala DSL for yabai configuration"

scalaVersion := "3.0.0"

scalacOptions += "-source:future"

libraryDependencies ++= Seq(
  ("io.circe"     %% "circe-parser" % "0.13.0").cross(CrossVersion.for3Use2_13),
  ("io.circe"     %% "circe-optics" % "0.13.0").cross(CrossVersion.for3Use2_13),
  "org.scalameta" %% "munit"        % "0.7.26" % Test
)

scalafmtOnCompile := true

scalafixOnCompile := true
ThisBuild / scalafixDependencies += "com.nequissimus" %% "sort-imports" % "0.5.5"

enablePlugins(AutomateHeaderPlugin)
startYear := Some(2020)
organizationName := "github.com/2m/yabai-scala/contributors"
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/2m/yabai-scala"))
developers := List(
  Developer(
    "contributors",
    "Contributors",
    "https://gitter.im/2m/general",
    url("https://github.com/2m/yabai-scala/graphs/contributors")
  )
)
sonatypeProfileName := "lt.dvim"
versionScheme := Some("semver-spec")
