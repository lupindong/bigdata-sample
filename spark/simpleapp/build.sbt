import sbt.util

ThisBuild / scalaVersion := "2.11.8"
ThisBuild / organization := "net.lovexq"
ThisBuild / version := "1.0"

logLevel := util.Level.Info

lazy val simpleApp = (project in file("."))
  .settings(
    name := "SimpleApp",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0"
  )