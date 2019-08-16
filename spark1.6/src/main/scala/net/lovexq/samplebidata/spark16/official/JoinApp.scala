package net.lovexq.samplebidata.spark16.official

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 连接操作
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object JoinApp {

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

    //extract (movieid,rating) 
    val rating = ratingsRDD
      .filter(!ratingsHeader.equals(_))
      .map(line => {
        val fileds = line.split(",")
        (fileds(1).toInt, fileds(2).toDouble)
      })

    //get (movieid,ave_rating)
    val movieScores = rating.groupByKey()
      .map(data => {
        val avg = data._2.sum / data._2.size
        (data._1, avg)
      })

    //get (MovieID,MovieName)
    val movieskey = moviesRDD
      .filter(!moviesHeader.equals(_))
      .map(line => {
        val fileds = line.split(",")
        (fileds(0).toInt, fileds(1))
      })
      .keyBy(tup => tup._1)

    // by join, we get <movie, averageRating, movieName> 
    val result = movieScores.keyBy(_._1)
      .join(movieskey)
      .filter(f => f._2._1._2 > 4.0)
      .map(f => (f._1, f._2._1._2, f._2._2._2))

    println(s"count is ${result.count()}")

    result.foreach(println)

    val end = System.currentTimeMillis()

    println(s"use time is ${end - begin}")

    sc.stop()
  }
}
