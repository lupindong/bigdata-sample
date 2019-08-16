package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的连接操作
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MyJoinApp {

  def main(args: Array[String]): Unit = {
    val begin = System.currentTimeMillis()

    // 默认数据目录
    var filePath = "file:////home/cloudera/data/ml-latest-small"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("Join Application")
    val sc = new SparkContext(conf)
    val moviesRDD = sc.textFile(filePath + "/movies.csv").cache
    val ratingsRDD = sc.textFile(filePath + "/ratings.csv").cache

    val moviesHeader = moviesRDD.first()
    val ratingsHeader = ratingsRDD.first()

    val rdd1 = moviesRDD
      .filter(moviesHeader != _)
      .map(e => {
        val arr = e.split(",")
        (arr(0).toInt, arr(1).trim)
      })

    // 计算平均分，过滤出超过4.0分的电影
    val rdd2 = ratingsRDD
      .filter(ratingsHeader != _)
      .map(e => {
        val arr = e.split(",")
        (arr(1).toInt, (arr(2).toDouble, 1))
      })
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
      .map(e => (e._1, e._2._1 / e._2._2))
      .filter(_._2 > 4.0)

    val result = rdd1.join(rdd2)

    println(s"count is ${result.count()}")

    result.foreach(println)

    val end = System.currentTimeMillis()

    println(s"use time is ${end - begin}")

    sc.stop()
  }
}
