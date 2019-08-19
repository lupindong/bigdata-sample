package net.lovexq.samplebidata.spark16.rdd.official

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.{HashPartitioner, SparkContext}

/**
  * 文件排序
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object FileSortApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home/cloudera/data/fileSort"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("FileSort Application")
    val sc = new SparkContext(conf)

    val lines = sc.textFile(filePath + "/input", 3)
    var index = 0
    val result = lines.filter(_.trim().length > 0)
      .map(n => (n.trim.toInt, ""))
      .partitionBy(new HashPartitioner(1))
      .sortByKey()
      .map(t => {
        index += 1
        (index, t._1)
      })

    result.saveAsTextFile(filePath + "/output")

    sc.stop()
  }
}
