package otus.kafka

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

import java.util.UUID


object Main extends App {
  println("Consumer")
  

  val spark = SparkSession.builder
    // .master("spark://WINDOWS-SERGCH.:7077")
    .master("local")
    .appName("Word Count")
    .getOrCreate()

  import spark.implicits._

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

  val df = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:29092")
    .option("subscribe", "books")
    .option("startingOffsets", "earliest")
    .load()
    .selectExpr("CAST(value AS STRING)")
    .as[(String)]
    .select(from_json(col("value"), schema).as("json_struct"))
    .select(col("json_struct.*"))

  val checkpointLocation = "/tmp/temporary-" + UUID.randomUUID.toString

  val query = df
    .where(col("Rating") >= 4.9)
    .writeStream
    .outputMode("append")
    .format("csv") // easier to debug with cat *.csv
    //.format("parquet")
    .option("checkpointLocation", checkpointLocation)
    .option("path", "/home/sergch/otus/kafka/hw/streaming_results")
    .start()

  query.awaitTermination()

}
