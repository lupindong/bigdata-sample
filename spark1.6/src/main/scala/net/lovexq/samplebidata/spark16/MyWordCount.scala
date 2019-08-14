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
