package net.lovexq.samplebidata.spark16.archive

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 我的单词计数
  * <br/>
  * 命令示例：spark-submit --class "net.lovexq.samplebidata.spark.MyWordCount" \
  * --master local[4] spark1-6_2.10-0.1.0-SNAPSHOT.jar /user/admin/lovexq/mapreduce/input/word.txt
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object MyWordCount {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("MyWordCount Application")

    val sc = new SparkContext(conf)

    val textFilePath = args(0)

    val logData = sc.textFile(textFilePath)

    logData.flatMap(_.split("\\W+"))
      .map((_, 1))
      .reduceByKey(_ + _)
      .sortByKey(true)
      .collect()
      .foreach(println)

    sc.stop()

  }
}
