ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "sergch"
ThisBuild / version := "1.0"


val sparkVersion = "3.5.0"
val provided = "provided"


ThisBuild / libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1",
  "org.apache.spark" %% "spark-sql" % sparkVersion % provided,
  "org.apache.spark" %% "spark-streaming" % sparkVersion % provided,
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion % provided
)

lazy val root = (project in file("."))
  .aggregate(source, consumer)


lazy val source = project

lazy val consumer = project