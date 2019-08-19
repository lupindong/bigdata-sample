package net.lovexq.samplebidata.spark16.sql

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

/**
  * SQL实验二<br/>
  * 数据格式如下所示：
  * ```
  * 1,Ella,36
  * 2,Bob,29
  * 3,Jack,29
  * ```
  *
  * @author LuPindong
  * @time 2019-08-08 21:43
  **/
object SQLTest2App {
  def main(args: Array[String]): Unit = {
    val conf = AppUtil.getSparkConf("SQLTest2 Application")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    import sqlContext.implicits._

    val df = sc.textFile("file:///home/cloudera/data/employee.txt")
      .filter(e => e != null && !e.trim.isEmpty)
      .map(e => {
        val arr = e.split(",")
        Employee(arr(0).toInt, arr(1), arr(2).toInt)
      }).toDF()

    df.registerTempTable("employee")

    sqlContext.sql("select id,name,age from employee")
      .map(r => {
        s"id:${r(0)},name:${r(1)},age:${r(2)}"
      })
      .sortBy(e => e(0))
      .foreach(println)

    sc.stop()
  }
}

case class Employee(id: Int, name: String, age: Int)
