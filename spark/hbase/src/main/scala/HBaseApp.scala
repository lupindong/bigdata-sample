import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

/**
  * HBase APP
  *
  * @author LuPindong
  * @time 2019-08-08 17:18
  */
object HBaseApp {

  def main(args: Array[String]): Unit = {
    val method = args(0)
    method match {
      case "Read" => read(args)
      case "Write" => write(args)
    }
  }

  /**
    * 读
    *
    * @param args
    */
  def read(args: Array[String]): Unit = {
    println("exec read...")

    val sparkConf = new SparkConf().setAppName("SparkReadHBase Application")

    val sc = new SparkContext(sparkConf)

    val hbaseConf = HBaseConfiguration.create()

    // 设置查询表名
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "student")

    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])

    // 获取记录数
    val count = hbaseRDD.count

    println(s">>>Students RDD Sount: ${count}")

    hbaseRDD.cache

    hbaseRDD.sample(false, 0.1)
      .foreachPartition(fp => {
        fp.foreach(r => {
          val key = Bytes.toString(r._2.getRow)
          val name = Bytes.toString(r._2.getValue("info".getBytes, "name".getBytes()))
          val gender = Bytes.toString(r._2.getValue("info".getBytes, "gender".getBytes()))
          val age = Bytes.toString(r._2.getValue("info".getBytes, "age".getBytes()))
          println(s"Row key: ${key}, name: ${name}, gender: ${gender}, age: ${age}")
        })
      })

    sc.stop()
  }

  /**
    * 写
    *
    * @param args
    */
  def write(args: Array[String]): Unit = {
    println("exec write...")

    val sparkConf = new SparkConf().setAppName("SparkWriteHBase Application").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val hadoopConfig = sc.hadoopConfiguration

    // 设置查询表名
    val tableName = "student"
    hadoopConfig.set(TableOutputFormat.OUTPUT_TABLE, tableName)

    val job = new Job(hadoopConfig)
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Result])
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])

    //构建两行记录
    val insertDataRDD = sc.makeRDD(Array("3,Rongcheng,M,26", "4,Guanhua,M,27"))

    val rdd = insertDataRDD.map(_.split(','))
      .map(arr => {
        val put = new Put(Bytes.toBytes(arr(0))) //行健的值
        put.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(arr(1))) //info:name列的值
        put.add(Bytes.toBytes("info"), Bytes.toBytes("gender"), Bytes.toBytes(arr(2))) //info:gender列的值
        put.add(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(arr(3).toInt)) //info:age列的值
        (new ImmutableBytesWritable, put)
      })

    rdd.saveAsNewAPIHadoopDataset(job.getConfiguration())

    sc.stop()
  }
}
