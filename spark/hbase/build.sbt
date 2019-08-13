import sbt.util

ThisBuild / scalaVersion := "2.11.8"
ThisBuild / organization := "net.lovexq"
ThisBuild / version := "1.0"

logLevel := util.Level.Info

lazy val hbase = (project in file("."))
  .settings(
    name := "hbase",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0",
    libraryDependencies += "org.apache.hbase" % "hbase" % "1.2.0",
    libraryDependencies += "org.apache.hbase" % "hbase-client" % "1.2.0",
    libraryDependencies += "org.apache.hbase" % "hbase-common" % "1.2.0",
    libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.2.0"
  )