ThisBuild / organization := "net.lovexq"
ThisBuild / scalaVersion := "2.10.7"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
lazy val sparkcore = "org.apache.spark" %% "spark-core" % "1.6.0"
lazy val sparksql = "org.apache.spark" %% "spark-sql" % "1.6.0"
lazy val sparkyarn = "org.apache.spark" %% "spark-yarn" % "1.6.0"


lazy val root = (project in file("."))
  .settings(
    name := "spark1.6",
    libraryDependencies += scalatest,
    libraryDependencies += sparkcore,
    libraryDependencies += sparksql,
    libraryDependencies += sparkyarn
  )