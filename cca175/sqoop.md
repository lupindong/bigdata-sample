# solution1

1.连接数据库
mysql --user=retail_dba --password=cloudera retail_db

2.查看数据表
show tables;

3.查看categories表的记录数
select count(1) from categories;

4.列举hdfs文件夹
hdfs dfs -ls;

5.不指定目录导入
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories

6.查看导入记录
hdfs dfs -cat categories/part-m*

7.指定目录导入
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--target-dir=categories_target

8.查看导入记录
hdfs dfs -cat categories_target/part-m*

9.指定父级目录导入
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_warehouse

10.检查导入记录
hdfs dfs -cat categories_warehouse/categories/part-m*

# solution2


