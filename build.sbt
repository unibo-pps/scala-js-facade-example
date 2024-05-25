ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"
enablePlugins(ScalaJSBundlerPlugin)
scalaJSUseMainModuleInitializer := true
Compile / npmDependencies += "neataptic" -> "1.4.7"
