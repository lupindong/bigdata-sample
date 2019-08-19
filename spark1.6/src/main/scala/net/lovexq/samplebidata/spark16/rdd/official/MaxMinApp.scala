package net.lovexq.samplebidata.spark16.rdd.official

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 最大最小值
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MaxMinApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home//data/maxmin"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("MaxMin Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath).cache

    dataRDD
      .filter(_.trim().length > 0)
      .map(line => ("key", line.trim.toInt))
      .groupByKey()
      .map(x => {
        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE
        for (num <- x._2) {
          if (num > max) {
            max = num
          }
          if (num < min) {
            min = num
          }
        }
        (max, min)
      }).collect.foreach(x => {
      println("max\t" + x._1)
      println("min\t" + x._2)
    })

    sc.stop()
  }
}
