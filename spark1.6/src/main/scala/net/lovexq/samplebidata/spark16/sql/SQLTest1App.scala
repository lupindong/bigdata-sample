package net.lovexq.samplebidata.spark16.sql

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

/**
  * SQL实验一<br/>
  * 数据格式如下所示：
  * ```
  * { "id":1 ,"name":"Ella","age":36 }
  * { "id":2, "name":"Bob","age":29 }
  * { "id":3 ,"name":"Jack","age":29 }
  * { "id":4 ,"name":"Jim","age":28 }
  * { "id":5 ,"name":"Damon" }
  * { "id":5 ,"name":"Damon" }
  * ……
  *
  * @author LuPindong
  * @time 2019-08-08 21:43
  **/
object SQLTest1App {
  def main(args: Array[String]): Unit = {
    val conf = AppUtil.getSparkConf("SQLTest1 Application")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val df = sqlContext.read.json("file:///home/cloudera/data/employee.json")

    // (1) 查询所有数据；
    df.show
    println("↑↑↑↑1.查询所有数据")

    // (2) 查询所有数据，并去除重复的数据；
    df.distinct().show
    println("↑↑↑↑2.查询所有数据，并去除重复的数据")

    // (3) 查询所有数据，打印时去除 id 字段；
    // df.select("name", "age").show
    df.drop("id").show
    println("↑↑↑↑3.查询所有数据，打印时去除 id 字段")

    // (4) 筛选出 age>30 的记录；
    df.filter(df("age") > 30).show
    println("↑↑↑↑4.筛选出 age>30 的记录")

    // (5) 将数据按 age 分组；
    df.groupBy(df("age")).count().show
    println("↑↑↑↑5.将数据按 age 分组")

    // (6) 将数据按 name 升序排列；
    df.sort(df("name").asc).show
    println("↑↑↑↑6.将数据按 name 升序排列")

    // (7) 取出前 3 行数据；
    // df.limit(3).show
    val rows = df.take(3)
    rows.foreach(println)
    df.head(3)
    println("↑↑↑↑7.取出前 3 行数据")

    // (8) 查询所有记录的 name 列，并为其取别名为 username；
    df.select(df("id"), df("name").as("username"), df("age")).show
    println("↑↑↑↑8.查询所有记录的 name 列，并为其取别名为 username")

    // (9) 查询年龄 age 的平均值；
    df.agg("age" -> "avg").show
    println("↑↑↑↑9.查询年龄 age 的平均值")

    // (10) 查询年龄 age 的最小值。
    df.agg("age" -> "min").show
    println("↑↑↑↑10.查询年龄 age 的最小值")

    sc.stop()
  }

}
