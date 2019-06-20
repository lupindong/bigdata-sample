# solution1

1.连接数据库
mysql --user=retail_dba --password=cloudera retail_db

2.查看数据表
show tables;

3.查看categories表的记录数
select count(1) from categories;

4.列举hdfs文件夹
hdfs dfs -ls;

5.不指定文件夹导入
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories

6.查看导入记录
hdfs dfs -cat categories/part-m*

7.指定文件夹导入
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--target-dir=categories_target

8.查看导入记录
hdfs dfs -cat categories_target/part-m*

9.指定父级文件夹导入
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_warehouse

10.检查导入记录
hdfs dfs -cat categories_warehouse/categories/part-m*

# solution2

1.检查可用命令
hdfs dfs

2.获取指定命令介绍
hdfs dfs -help get

3.创建Hdfs文件夹
hdfs dfs -mkdir Employee

4.进入本地存放数据的文件夹，并上传至hdfs
cd /home/cloudera/lupindong
hdfs dfs -put -f Employee

5.检查文件夹内容
hdfs dfs -ls Employee

6.合并文件夹下所有文件
hdfs dfs -getmerge -nl Employee MergedEmployee.txt

7.查看合并文件内容
cat MergedEmployee.txt

8.上传至hdfs
hdfs dfs -put MergedEmployee.txt Employee/

9.检查文件夹内容
hdfs dfs -ls Employee

10.修改文件权限
hdfs dfs -chmod 644 Employee/MergedEmployee.txt

11.复制hdfs文件夹到本地
hdfs dfs -get Employee Employee_hdfs