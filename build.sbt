ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.7"
ThisBuild / name := "scalajs-facade-example"

lazy val root = (project in file("."))
  .aggregate(handcrafted, scalablytyped)
  .settings(publish / skip := true)

lazy val handcrafted = (project in file("handcrafted"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "scala-js-facade-handcrafted",
    scalaJSUseMainModuleInitializer := true,
    Compile / npmDependencies += "@tensorflow/tfjs" -> "4.22.0"
  )

lazy val scalablytyped = (project in file("scalablytyped"))
  .enablePlugins(ScalaJSBundlerPlugin, ScalablyTypedConverterGenSourcePlugin)
  .settings(
    name := "scala-js-facade-scalablytyped",
    scalaJSUseMainModuleInitializer := true,
    Compile / npmDependencies += "@tensorflow/tfjs" -> "4.22.0",
    stOutputPackage := "customtypings",
    Compile / webpackConfigFile := Some(baseDirectory.value / "webpack.config.js"),
    Compile / run := {
      val webpackFiles = (Compile / fastOptJS / webpack).value
      val bundleFile = webpackFiles.head.data
      import scala.sys.process.*
      val exitCode = Process(Seq("node", bundleFile.getAbsolutePath), baseDirectory.value).!
      if (exitCode != 0) sys.error(s"Node execution failed with exit code $exitCode")
    }
  )
