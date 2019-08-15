package net.lovexq.samplebidata

import org.apache.spark.SparkConf

/**
  * ${DESCRIPTION}
  *
  * @author LuPindong
  * @time 2019-08-14 16:36
  */
object AppUtil {
  val WINDOWS: Boolean = System.getProperty("os.name").startsWith("Windows")

  def setEnvProperty(): Unit = {
    if (WINDOWS) {
      System.getProperties.setProperty("hadoop.home.dir", "D:\\Development\\hadoop-2.6.5")
      System.getProperties.setProperty("HADOOP_USER_NAME", "cloudera")
    }
  }

  def getSparkConf(appName: String): SparkConf = {
    setEnvProperty()
    val conf = new SparkConf().setAppName(appName)
    if (WINDOWS) {
      conf.setMaster("local[*]")
    }
    conf
  }
}
