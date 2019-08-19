package net.lovexq.samplebidata.spark16.rdd

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的最大最小值
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MyMaxMinApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home//data/maxmin"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("MaxMin Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath).cache

    val max = dataRDD
      .filter(e => e != null && !e.isEmpty)
      .map(_.trim.toInt)
      .max()

    val min = dataRDD
      .filter(e => e != null && !e.isEmpty)
      .map(_.trim.toInt)
      .min()

    println(s"max is ${max}, min is ${min}")

    sc.stop()
  }
}
