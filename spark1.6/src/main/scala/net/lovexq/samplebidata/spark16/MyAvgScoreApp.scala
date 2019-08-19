package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的平均分
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MyAvgScoreApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home/cloudera/data/avgscore"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("AvgScore Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath + "/input").cache

    val result = dataRDD
      .filter(e => e != null && !e.trim.isEmpty)
      .map(e => {
        val arr = e.split(" ")
        (arr(0), arr(1).toDouble)
      })
      .combineByKey(
        (e) => (e, 1),
        (acc: (Double, Int), e) => (acc._1 + e, acc._2 + 1),
        (acc1: (Double, Int), acc2: (Double, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2),
        numPartitions = 1
      )
      .map {
        case (k, v) => (k, v._1 / v._2)
      }

    result.saveAsTextFile(filePath + "/output")

    sc.stop()
  }
}
