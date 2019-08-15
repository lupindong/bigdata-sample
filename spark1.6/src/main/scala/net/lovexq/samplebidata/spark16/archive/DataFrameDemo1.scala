package net.lovexq.samplebidata.spark16.archive

import org.apache.spark.{SparkConf, SparkContext}

/**
  * DataFrame Demo
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object DataFrameDemo1 {
  def main(args: Array[String]): Unit = {
    val appName = "DataFrameDemo Application"

    val master = "local"

    val conf = new SparkConf().setAppName(appName).setMaster(master)

    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    // Create the DataFrame
    val df = sqlContext.read.json("file:///D:\\WorkSpaces\\2019\\bigdata-sample\\spark1.6\\" +
      "src\\main\\resources\\data\\people.json")

    // Show the content of the DataFrame
    df.show()

    // Print the schema in a tree format
    df.printSchema()

    // Select only the "name" column
    df.select("name").show()

    // Select everybody, but increment the age by 1
    df.select(df("name"), df("age") + 1).show()

    // Select people older than 21
    df.filter(df("age") > 21).show()

    // Count people by age
    df.groupBy("age").count().show()

    sc.stop()
  }
}
