scalaVersion := "2.13.12"
organization := "sergch"
version := "1.0"


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"

val sparkVersion = "3.5.0"
val provided = "provided"

val commonLibrarayDependencied = Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
)


lazy val root = (project in file(".")).settings(
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
  ),
  name := "consumer"

)