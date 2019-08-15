package net.lovexq.samplebidata.spark16.archive

import org.apache.spark.{SparkConf, SparkContext}

/**
  * DataFrame Demo
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object DataFrameDemo4 {

  /**
    * Programmatically Specifying the Schema
    * <br/>
    * 1.Create an RDD of Rows from the original RDD;
    * 2.Create the schema represented by a StructType matching the structure of Rows in the RDD created in Step 1.
    * 3.Apply the schema to the RDD of Rows via createDataFrame method provided by SQLContext.
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val appName = "DataFrameDemo Application"

    val master = "local"

    val conf = new SparkConf().setAppName(appName).setMaster(master)

    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    // Create an RDD
    val path = "file:///D:\\WorkSpaces\\2019\\bigdata-sample\\spark1.6\\src\\main\\resources\\data\\people.txt"
    val people = sc.textFile(path)

    // Import Row.
    import org.apache.spark.sql.Row

    // Convert records of the RDD (people) to Rows.
    val rowRDD = people.map(_.split(",")).map(p => Row(p(0), p(1).trim))

    // Import Spark SQL data types
    import org.apache.spark.sql.types.{StringType, StructField, StructType};

    // The schema is encoded in a string
    val schemaString = "name,age"

    // Generate the schema based on the string of schema
    val schema = StructType(
      schemaString
        .split(",")
        .map(fieldName => StructField(fieldName, StringType, true))
    )

    // Apply the schema to the RDD.
    val peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema)

    // Register the DataFrames as a table.
    peopleDataFrame.registerTempTable("peopley")


    // SQL statements can be run by using the sql methods provided by sqlContext.
    val results = sqlContext.sql("SELECT name FROM peopley")

    // The results of SQL queries are DataFrames and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by field index or by field name.
    results.map(t => "Name: " + t(0)).collect().foreach(println)

    sc.stop()
  }
}
