package source

import org.apache.spark.sql.{SaveMode, SparkSession, functions, DataFrame, Row}

import org.apache.spark.sql.functions._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{
  StructType,
  StringType,
  StructField,
  DoubleType,
  IntegerType
}
import org.apache.spark.sql.Encoders

object Source extends App {
  println("Source")

  val spark = SparkSession.builder
    // .master("spark://WINDOWS-SERGCH.:7077")
    .master("local")
    .appName("CSV to JSON")
    .getOrCreate()

  val schema = StructType(
    Array(
      StructField("Name", StringType, true),
      StructField("Author", StringType, true),
      StructField("Rating", DoubleType, true),
      StructField("Reviews", IntegerType, true),
      StructField("Price", IntegerType, true),
      StructField("Year", IntegerType, true),
      StructField("Genre", StringType, true)
    )
  )

  val df =
    spark.read
      .schema(schema)
      .csv("/home/sergch/otus/kafka/hw/bestsellers with categories.csv")

  df.map(_.json, Encoders.STRING)
    .select(col("value"))
    .write
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:29092")
    .option("topic", "books")
    .save()

}
