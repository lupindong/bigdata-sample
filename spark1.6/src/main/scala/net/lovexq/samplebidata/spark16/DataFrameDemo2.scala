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
  * DataFrame Demo
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object DataFrameDemo2 {

  /**
    * Creating Datasets
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val appName = "DataFrameDemo Application"

    val master = "local"

    val conf = new SparkConf().setAppName(appName).setMaster(master)

    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    import sqlContext.implicits._

    // Encoders for most common types are automatically provided by importing sqlContext.implicits._
    val dsq = Seq(1, 2, 3).toDS()
    val arr = dsq.map(_ + 1).collect() // Returns: Array(2, 3, 4)

    // Encoders are also created for case classes.
    val ds = Seq(Person("Andy", 32)).toDS()

    // DataFrames can be converted to a Dataset by providing a class. Mapping will be done by name.
    val path = "file:///D:\\WorkSpaces\\2019\\bigdata-sample\\spark1.6\\src\\main\\resources\\data\\people.json"
    val people = sqlContext.read.json(path).as[Person]

    sc.stop()
  }
}

case class Person(name: String, age: Long)
