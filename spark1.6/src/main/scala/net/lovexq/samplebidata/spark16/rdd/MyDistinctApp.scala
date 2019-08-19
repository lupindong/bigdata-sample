package net.lovexq.samplebidata.spark16.rdd

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的去重
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MyDistinctApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home/cloudera/data/distinct"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("Distinct Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath + "/input").cache

    val result = dataRDD
      .map(e => (e.trim, 1))
      .distinct(1)
      .sortByKey(true)
      .keys

    result.saveAsTextFile(filePath + "/output")

    sc.stop()
  }
}
