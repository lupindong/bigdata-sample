/*
 * Copyright 2019 lupindong@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.lovexq.samplebidata.spark16

import net.lovexq.samplebidata.AppUtil
import org.apache.spark.{SparkConf, SparkContext}

/**
  * ${DESCRIPTION}
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object SimpleApp {
  def main(args: Array[String]): Unit = {

    AppUtil.setEnv()

    val appName = "Simple Application"

    val master = "yarn-client"

    val conf = new SparkConf().setAppName(appName).setMaster(master)

    val sc = new SparkContext(conf)

    val textFilePath = "hdfs://quickstart:8020/user/cloudera/word.txt"

    val logData = sc.textFile(textFilePath).cache()

    val numAs = logData.filter(_.contains("a")).count()

    val numBs = logData.filter(_.contains("b")).count()

    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

    sc.stop()

  }
}
