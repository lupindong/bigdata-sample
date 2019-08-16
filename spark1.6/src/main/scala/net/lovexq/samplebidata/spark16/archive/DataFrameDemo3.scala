package net.lovexq.samplebidata.spark16.archive

import org.apache.spark.{SparkConf, SparkContext}

/**
  * DataFrame Demo
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object DataFrameDemo3 {

  /**
    * Inferring the Schema Using Reflection
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val appName = "DataFrameDemo Application"

    val master = "local"

    val conf = new SparkConf().setAppName(appName).setMaster(master)

    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    import sqlContext.implicits._

    // Create an RDD of Person objects and register it as a table.
    val path = "file:///D:\\WorkSpaces\\2019\\bigdata-sample\\spark1.6\\src\\main\\resources\\data\\people.txt"
    val people = sc.textFile(path)
      .map(_.split(","))
      .map(p => PersonX(p(0), p(1).trim.toLong))
      .toDF()

    people.registerTempTable("peoplex")

    // SQL statements can be run by using the sql methods provided by sqlContext.
    val teenagers = sqlContext.sql("select name, age from peoplex where age >= 13 and age <=19")

    // The results of SQL queries are DataFrames and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by field index:
    teenagers.map(t => "Name: " + t(0)).collect().foreach(println)

    // or by field name:
    teenagers.map(t => "Name: " + t.getAs[String]("name")).collect().foreach(println)

    // row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
    teenagers.map(_.getValuesMap[Any](List("name", "age"))).collect().foreach(println)
    // Map("name" -> "Justin", "age" -> 19)

    sc.stop()
  }
}

case class PersonX(name: String, age: Long)
