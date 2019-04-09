# Spark1.6 Guide

LanceLand <<lupindong@gmail.com>> ,v0.0.1,2019.04.08

[TOC]

# [Quick Start](https://spark.apache.org/docs/1.6.0/quick-start.html)

## 使用Spark Shell进行交互式分析
### 基本
```shell
$> spark-shell
# 载入HDFS数据创建RDD
scala> val textFile = sc.textFile("hdfs://server2:8020/user/admin/lovexq/mapreduce/input")
textFile: org.apache.spark.rdd.RDD[String] = hdfs://server2:8020/user/admin/lovexq/mapreduce/input MapPartitionsRDD[9] at textFile at <console>:27
# 此RDD中的项目数
scala> textFile.count()
res2: Long = 10

# 此RDD中的第一项
scala> textFile.first()
res3: String = hadoop hdfs hive spark

# 过滤出含有spark的行
scala> val linesWithSpark = textFile.filter(_.contains("spark"))
linesWithSpark: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[10] at filter at <console>:29

# 统计含有spark的行数
scala> textFile.filter(_.contains("spark")).count
res11: Long = 7
```

### 有关RDD操作的更多信息

```shell
# 找出最多单词的一行
scala> textFile.map(_.split(" ").size).reduce((a, b) => if (a > b) a else b) 
res13: Int = 7

# 导入并使用Math库
scala> import java.lang.Math
import java.lang.Math

scala> textFile.map(_.split(" ").size).reduce(Math.max(_,_)) 
res11: Long = 7

# wordcount例子
scala> textFile.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).collect()
```

### 高速缓存
```shell
# 缓存RDD
scala> linesWithSpark.cache()
res20: linesWithSpark.type = MapPartitionsRDD[18] at filter at <console>:29

scala> linesWithSpark.count()
res21: Long = 7

scala> linesWithSpark.count()
res22: Long = 7

```