package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的实验一<br/>
  * 数据格式如下所示：
  * ```
  * Tom,DataBase,80
  * Tom,Algorithm,50
  * Tom,DataStructure,60
  * Jim,DataBase,90
  * Jim,Algorithm,60
  * Jim,DataStructure,80
  * ……
  *
  * @author LuPindong
  * @time 2019-08-08 21:43
  **/
object MyTest1App {
  def main(args: Array[String]): Unit = {
    val conf = AppUtil.getSparkConf("Test1 Application")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("file:///home/cloudera/data/chapter5-data1.txt").cache

    val dataRdd = lines.map(_.split(","))

    //（1）该系总共有多少学生；265
    val stuCount = dataRdd
      .map(_ (0))
      .distinct()
      .count()
    println(s"该系总共有多少学生: ${stuCount}")

    //（2）该系共开设来多少门课程；8
    val clasCount = dataRdd
      .map(_ (1))
      .distinct()
      .count()
    println(s"该系共开设来多少门课程: ${clasCount}")

    //（3）Tom同学的总成绩平均分是多少；
    val tomRec = dataRdd
      .filter("Tom" == _ (0).trim)
      .map(_ (2).toDouble)
      .collect()
    val tomAvg = tomRec.reduce(_ + _) / tomRec.length
    println(s"Tom同学的总成绩平均分是多少: ${tomAvg}")

    //（4）求每名同学的选修的课程门数；
    println("求每名同学的选修的课程门数如下:")
    dataRdd
      .map(e => (e(0), 1))
      .reduceByKey((x, y) => x + y)
      .sortByKey(ascending = true, numPartitions = 1)
      .foreach(println)

    //（5）该系DataBase课程共有多少人选修；
    val dbCount = dataRdd
      .filter("DataBase" == _ (1).trim)
      .count()
    println(s"该系DataBase课程共有多少人选修: ${dbCount}")

    //（6）各门课程的平均分是多少；
    println("各门课程的平均分是多少如下:")
    dataRdd
      .map(e => (e(1), e(2).toDouble))
      .combineByKey(
        (e) => (e, 1),
        (acc: (Double, Int), e) => (acc._1 + e, acc._2 + 1),
        (acc1: (Double, Int), acc2: (Double, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
      )
      .map { case (k, v) => (k, v._1 / v._2) }
      .foreach(println)

    //（7）使用累加器计算共有多少人选了 DataBase 这门课。
    val dbAcc = sc.accumulator(0L, "DataBase")
    dataRdd.map(e => {
      if ("DataBase" == e(1).trim) {
        dbAcc.add(1L)
      }
    }).collect

    println(s"使用累加器计算共有多少人选了DataBase这门课: ${dbAcc.value}")

    sc.stop()
  }
}
