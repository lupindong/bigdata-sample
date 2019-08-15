package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * 我的二次排序
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object MySecondarySortApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home/cloudera/data/secondarySort/file1.txt"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("SecondarySort Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath).cache

    dataRDD
      .filter(e => e != null && !e.isEmpty)
      .map(e => {
        val arr = e.split(" ").filter(!_.isEmpty)
        (new SecondarySortKey(arr(0).toInt, arr(1).toInt), e)
      })
      .sortByKey(false, 1)
      .map(_._2)
      .foreach(println)

    sc.stop()
  }
}
