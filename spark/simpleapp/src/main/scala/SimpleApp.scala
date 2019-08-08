import org.apache.spark.{SparkConf, SparkContext}

/**
  * 示例APP
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object SimpleApp {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Simple Application")

    val sc = new SparkContext(conf)

    val logFile = "file:///usr/local/spark/README.MD"

    val logData = sc.textFile(logFile, 2).cache

    val numAs = logData.filter(_.contains("a")).count

    val numBs = logData.filter(_.contains("b")).count

    println(s"Lines with a: ${numAs}, Lines with b: ${numBs}")

    sc.stop()
  }
}
