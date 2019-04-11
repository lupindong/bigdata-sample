# **[Problem 1:](<https://arun-teaches-u-tech.blogspot.com/p/cca-175-prep-problem-scenario-1.html>)**

1. Using sqoop, import orders table into hdfs to folders **/user/cloudera/problem1/orders**. File should be loaded as Avro File and use snappy compression

2. Using sqoop, import order_items  table into hdfs to folders **/user/cloudera/problem1/order-items**. Files should be loaded as avro file and use snappy compression

3. Using Spark Scala load data at **/user/cloudera/problem1/orders** and **/user/cloudera/problem1/order-items** items as *dataframes*. 

4. Expected Intermediate Result:

    order_date , order_status, total_amount, total_orders. In plain english, please find total orders and total amount per status per day. The result should be sorted by order date in descending, order status in ascending and total amount in descending and total orders in ascending. Aggregation should be done using below methods. However, sorting can be done using a dataframe or RDD. Perform aggregation in each of the following ways

   - a). Just by using Data Frames API - here order_date should be YYYY-MM-DD format
   - b). Using Spark SQL  - here order_date should be YYYY-MM-DD format
   - c). By using combineByKey function on RDDS -- No need of formatting order_date or total_amount

5.  Store the result as parquet file into hdfs using gzip compression under folder

   - /user/cloudera/problem1/result4a-gzip
   - /user/cloudera/problem1/result4b-gzip
   - /user/cloudera/problem1/result4c-gzip

6.  Store the result as parquet file into hdfs using snappy compression under folder

   - /user/cloudera/problem1/result4a-snappy
   - /user/cloudera/problem1/result4b-snappy
   - /user/cloudera/problem1/result4c-snappy

7. Store the result as CSV file into hdfs using No compression under folder

   - /user/cloudera/problem1/result4a-csv
   - /user/cloudera/problem1/result4b-csv
   - /user/cloudera/problem1/result4c-csv

8. create a mysql table named result and load data from **/user/cloudera/problem1/result4a-csv** to mysql table named result 

# My Answer

1. Using sqoop, import orders table into hdfs to folders **/user/cloudera/problem1/orders**. File should be loaded as Avro File and use snappy compression

```shell
$ sqoop import \
--connect "jdbc:mysql://server2:13306/retail_db" \
--username retail_dba \
--password cloudera \
--table orders \
--target-dir "/user/cloudera/problem1/orders" \
--delete-target-dir \
--compress \
--compression-codec snappy \
--as-avrodatafile;
```
2. Using sqoop, import order_items  table into hdfs to folders **/user/cloudera/problem1/order-items**. Files should be loaded as avro file and use snappy compression

```shell
$ sqoop import \
--connect "jdbc:mysql://server2:13306/retail_db" \
--username retail_dba \
--password cloudera \
--table order_items \
--target-dir /user/cloudera/problem1/order-items \
--delete-target-dir \
--compress \
--compression-codec snappy \
--as-avrodatafile;
```
3. Using Spark Scala load data at **/user/cloudera/problem1/orders** and **/user/cloudera/problem1/order-items** items as *dataframes*. 
```shell
spark-shell

import com.databricks.spark.avro._

val ordersDF = sqlContext.read.avro("/user/cloudera/problem1/orders")

val orderItemsDF = sqlContext.read.avro("/user/cloudera/problem1/order-items")

```

4. Expected Intermediate Result:

    order_date , order_status, total_amount, total_orders.  In plain english, please find total orders and total amount per status per day. The result should be sorted by **order date in descending**, **order status in ascending** and **total amount in descending** and **total orders in ascending**. Aggregation should be done using below methods. However, sorting can be done using a dataframe or RDD. Perform aggregation in each of the following ways

   - a). Just by using Data Frames API - here order_date should be YYYY-MM-DD format
   - b). Using Spark SQL  - here order_date should be YYYY-MM-DD format
   - c). By using combineByKey function on RDDS -- No need of formatting order_date or total_amount

```shell
## 求每天各种状态的订单总数和订单金额
## 先join表，再按时间、状态分组group之后对订单数和订单金额分别进行聚合agg(求和sum)，再根据要求排序

# 4.0 
val joinDF = ordersDF.join(orderItemsDF, ordersDF("order_id") === orderItemsDF("order_item_order_id"))

joinDF.cache

import org.apache.spark.sql.functions._

# 4.a). Just by using Data Frames API - here order_date should be YYYY-MM-DD format
val dfResult = joinDF.groupBy(to_date(from_unixtime($"order_date"/1000)).alias("order_date_format"),$"order_status").agg(round(sum($"order_item_subtotal"),2).alias("total_amount"),countDistinct($"order_id").alias("total_orders")).orderBy(($"order_date_format").desc, ($"order_status").asc, ($"total_amount").desc, ($"total_orders").asc)

dfResult.show()

# 4.b). Using Spark SQL  - here order_date should be YYYY-MM-DD format
joinDF.registerTempTable("join_table")

val sql = "select to_date(from_unixtime(order_date/1000)) as order_date_format, order_status, cast(sum(order_item_subtotal) as decimal(10,2)) as total_amount, count(distinct order_id) as total_orders from join_table group by order_date, order_status order by order_date_format desc, order_status asc, total_amount desc, total_orders asc"

val sqlResult = sqlContext.sql(sql)

sqlResult.show()

# 4.c). By using combineByKey function on RDDS -- No need of formatting order_date or total_amount
joinDF.printSchema()

##root
## 0|-- order_id: integer (nullable = true)
## 1|-- order_date: long (nullable = true)
## 2|-- order_customer_id: integer (nullable = true)
## 3|-- order_status: string (nullable = true)
## 4|-- order_item_id: integer (nullable = true)
## 5|-- order_item_order_id: integer (nullable = true)
## 6|-- order_item_product_id: integer (nullable = true)
## 7|-- order_item_quantity: integer (nullable = true)
## 8|-- order_item_subtotal: float (nullable = true)
## 9|-- order_item_product_price: float (nullable = true)

## def combineByKey[C](
##       createCombiner: V => C,
##       mergeValue: (C, V) => C,
##       mergeCombiners: (C, C) => C,
##       partitioner: Partitioner,
##       mapSideCombine: Boolean = true,
##       serializer: Serializer = null)
      
# TODO

val combineResult = joinDF.
map(x=>((x(1).toString, x(3).toString), (x(8).toString.toFloat, x(0).toString))).
combineByKey((x:(Float, String))=>(x._1, Set(x._2)),
(x:(Float, Set[String]), y:(Float, String))=>(x._1+y._1, x._2+y._2),
(x:(Float, Set[String]), y:(Float, Set[String]))=>(x._1+y._1, x._2++y._2)).
map(x=>(x._1._1,x._1._2,x._2._1,x._2._2.size)).
toDF().
orderBy($"_1".desc,$"_2".asc,$"_3".desc,$"_4".asc)

combineResult.show()

val combineResult = joinDF.map(x=>((x(1).toString, x(3).toString), (x(8).toString.toFloat, x(0).toString))).combineByKey((x:(Float, String))=>(x._1, Set(x._2)),(x:(Float, Set[String]), y:(Float, String))=>(x._1+y._1, x._2+y._2),(x:(Float, Set[String]), y:(Float, Set[String]))=>(x._1+y._1, x._2++y._2)).collect()

```



















































