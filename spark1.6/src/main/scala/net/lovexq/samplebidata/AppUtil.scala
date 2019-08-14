package net.lovexq.samplebidata

/**
  * ${DESCRIPTION}
  *
  * @author LuPindong
  * @time 2019-08-14 16:36
  */
object AppUtil {
  val WINDOWS: Boolean = System.getProperty("os.name").startsWith("Windows")

  def setEnv(): Unit = {
    if (WINDOWS) {
      System.getProperties.setProperty("hadoop.home.dir", "D:\\Development\\hadoop-common-2.2.0")
      System.getProperties.setProperty("HADOOP_USER_NAME", "cloudera")
    }
  }
}
