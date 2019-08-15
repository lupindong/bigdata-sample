package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的TopN
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MyTopNApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home//data/topn"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    // 默认取前5名
    var n = 5
    if (!args.isEmpty && args.length > 1) {
      n = args(0).toInt
    }

    val conf = AppUtil.getSparkConf("TopN Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath).cache

    dataRDD
      // 过滤出非空行，且有四列的数据
      .filter(e => e != null && !e.trim.isEmpty && e.trim.split(",").length == 4)
      // 按逗号分割每行数据，取payment的值
      .map(_.trim.split(",")(2).trim().toInt)
      .sortBy(e => e, false)
      .take(n)
      .foreach(println)

    sc.stop()
  }
}
