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

package net.lovexq.samplebidata.spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * DataFrameDemo
  *
  * @author LuPindong
  * @time 2019-04-08 21:43
  */
object DataFrameDemo {
  def main(args: Array[String]): Unit = {
    val appName = "DataFrameDemo Application"

    val master = "local"

    val conf = new SparkConf().setAppName(appName).setMaster(master)

    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    // Create the DataFrame
    val df = sqlContext.read.json("file:///D:\\WorkSpaces\\2019\\bigdata-sample\\spark1.6\\" +
      "src\\main\\resources\\data\\people.json")

    // Show the content of the DataFrame
    df.show()

    // Print the schema in a tree format
    df.printSchema()

    // Select only the "name" column
    df.select("name").show()

    // Select everybody, but increment the age by 1
    df.select(df("name"), df("age") + 1).show()

    // Select people older than 21
    df.filter(df("age") > 21).show()

    // Count people by age
    df.groupBy("age").count().show()

    sc.stop()
  }
}
