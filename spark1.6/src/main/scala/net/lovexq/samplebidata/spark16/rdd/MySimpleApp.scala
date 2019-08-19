package net.lovexq.samplebidata.spark16.rdd

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.SparkContext

/**
  * ${DESCRIPTION}
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object MySimpleApp {
  def main(args: Array[String]): Unit = {

    val conf = AppUtil.getSparkConf("Simple Application")

    val sc = new SparkContext(conf)

    val textFilePath = "file:///home/cloudera/data/word.txt"

    val logData = sc.textFile(textFilePath).cache()

    val numAs = logData.filter(_.contains("a")).count()

    val numBs = logData.filter(_.contains("b")).count()

    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

    sc.stop()

  }
}
