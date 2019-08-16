package net.lovexq.samplebidata.spark16.official

import net.lovexq.samplebidata.AppUtil
import net.lovexq.samplebidata.support.SecondarySortKey
import org.apache.spark.SparkContext

/**
  * 二次排序
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object SecondarySortApp {

  def main(args: Array[String]): Unit = {
    // 默认数据目录
    var filePath = "file:////home/cloudera/data/secondarySort/file1.txt"
    if (!args.isEmpty) {
      filePath = args(0)
    }

    val conf = AppUtil.getSparkConf("SecondarySort Application")
    val sc = new SparkContext(conf)
    val dataRDD = sc.textFile(filePath).cache

    val pairWithSortKey = dataRDD.map(line => (new SecondarySortKey(line.split(" ")(0).toInt, line.split(" ")(1).toInt), line))
    val sorted = pairWithSortKey.sortByKey(false)
    val sortedResult = sorted.map(sortedLine => sortedLine._2)
    sortedResult.collect().foreach(println)

    sc.stop()
  }
}
