package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的文件排序
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MyFileSortApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home/cloudera/data/fileSort"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("FileSort Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath + "/input").cache

    var index = 0;
    val result = dataRDD
      .filter(e => e != null && !e.isEmpty)
      .map(e => (e.trim.toInt, ""))
      .sortByKey(numPartitions = 1)
      .map(e => {
        index += 1
        (index, e._1)
      })

    result.saveAsTextFile(filePath + "/output")

    sc.stop()
  }
}
