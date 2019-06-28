# solution1 
## 问题
**1.连接mysql db，并检查表内容**  
**2.将"retail_db.categories"表复制到hdfs，而不指定目录名**  
**3.将"retail_db.categories"表复制到hdfs，存储在目录“categories_target”中**    
**4.将"retail_db.categories"表复制到hdfs，存储在仓库目录“categories_warehouse”中**  


## 指令

| 指令                | 作用  |
| ------------------- | --------------------------------------- |
| --target-dir        | HDFS destination dir                    |
| --warehouse-dir     | HDFS parent for table destination       |

## 脚本
**setp1: **连接mysql db，并检查表内容  
mysql --user=retail_dba --password=cloudera retail_db  

show tables;  

select count(1) from categories;  

**setp2: **将"retail_db.categories"表复制到hdfs，而不指定目录名  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories  

hdfs dfs -cat categories/part-m*  

**setp3: **将"retail_db.categories"表复制到hdfs，存储在目录“categories_target”中  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--target-dir=categories_target  

hdfs dfs -cat categories_target/part-m*  

**setp4: **将"retail_db.categories"表复制到hdfs，存储在仓库目录“categories_warehouse”中  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_warehouse  

hdfs dfs -cat categories_warehouse/categories/part-m*  

# solution2 
## 问题
**1.您将使用哪个命令检查HDFS上的所有可用命令行选项，以及如何获得单个命令的帮助**  
**2.使用命令行创建名为Employee的新空目录。并创建一个名为quicktechie.txt的空文件**  
**3.在Employee目录中加载两份公司的员工数据（如何覆盖HDFS中的现有文件）**    
**4.将两份员工数据合并到一个名为MergeEmployee.txt的文件中，合并文件在每个文件内容的末尾都应具有新行字符**  
**5.在hdfs上上传合并文件，并更改HDFS合并文件的文件权限，以便所有者和组成员可以读写，其他用户可以读取该文件**    
**6.编写命令将单个文件以及整个目录从HDFS导出到本地文件系统**  

## 指令

| 指令                | 作用  |
| ------------------- | --------------------------------------- |
| -touchz        | Creates a file of zero length at <path> with current time as the timestamp of that <path> |
| -getmerge | Get all the files in the directories that match the source file pattern and merge and sort them to only one file on local fs. <src> is kept |

## 脚本
**setp1: **您将使用哪个命令检查hdfs上的所有可用命令行选项，以及如何获得单个命令的帮助  
hdfs dfs    

hdfs dfs -help get  

**setp2: **使用命令行创建名为employee的新空目录。并创建一个名为quicktechie.txt的空文件   
hdfs dfs -mkdir Employee  

hdfs dfs -touchz Employee/quicktechie.txt  

**setp3: **在Employee目录中加载两份公司的员工数据（如何覆盖HDFS中的现有文件）   
cd /home/cloudera/Employee  

hdfs dfs -put -f Employee  

hdfs dfs -ls Employee  

**setp4: ** 将两份员工数据合并到一个名为MergeEmployee.txt的文件中，合并文件在每个文件内容的末尾都应具有新行字符  
hdfs dfs -getmerge -nl Employee MergedEmployee.txt  

cat MergedEmployee.txt  

**setp5: **在hdfs上上传合并文件，并更改hdfs合并文件的文件权限，以便所有者和组成员可以读写，其他用户可以读取该文件     
hdfs dfs -put MergedEmployee.txt Employee/  

hdfs dfs -ls Employee  

hdfs dfs -chmod 644 Employee/MergedEmployee.txt  

**setp6: **编写命令将单个文件以及整个目录从HDFS导出到本地文件系统  

hdfs dfs -get Employee Employee_hdfs  


# solution3
## 问题
**1.导入category_id=22的数据到categories_subset目录  **
**2.导入category_id>22的数据到categories_subset_2目录  **
**3.导入category_id 从1到22的数据到categories_subset_3目录  **
**4.导入category_id 从1到22的数据到categories_subset_6目录 ，分隔符使用"|" **
**5.导入category_id 从1到22的数据到categories_subset_6目录 ，并限定只有category_name, category_id两列，分隔符使用"|"  **
**6.使用下列sql添加null值记录进表  **
**7.导入category_id 从1到61的数据到categories_subset_17目录 ，分隔符使用"|"，并对字符串和非字符串的null值进行转码  **
**8.导入全部表结构到categories_subset_all_tables目录 **

## 指令

| 指令                | 作用  |
| ------------------- | --------------------------------------- |
| --where | WHERE clause to use during import |
| --fields-terminated-by | Sets the field separator character |
| --null-string | The string to be written for a null value for string columns |
| --null-non-string | The string to be written for a null value for non-string columns |
## 脚本

**setp1: ** 导入category_id=22的数据到categories_subset目录  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_subset \
--where="category_id=22"  

hdfs dfs -cat categories_subset/categories/part*  

**setp2: ** 导入category_id>22的数据到categories_subset_2目录  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_subset_2 \
--where="category_id>22"  

hdfs dfs -cat categories_subset_2/categories/part*  

**setp3: ** 导入category_id 从1到22的数据到categories_subset_3目录  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_subset_3 \
--where="category_id between 1 and 22"  

hdfs dfs -cat categories_subset_3/categories/part*  

**setp4: ** 导入category_id 从1到22的数据到categories_subset_6目录 ，分隔符使用"|"  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_subset_6 \
--where="category_id between 1 and 22" \
--fields-terminated-by="|"  

hdfs dfs -cat categories_subset_6/categories/part*  

**setp5: ** 导入category_id 从1到22的数据到categories_subset_6目录 ，并限定只有category_name, category_id两列，分隔符使用"|"  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_subset_col \
--where="category_id between 1 and 22" \
--fields-terminated-by="|" \
--columns="category_name, category_id"  

**setp6: ** 使用下列sql添加null值记录进表  
alter table categories modify category_department_id int(11);  
insert into categories values(60,null,'TESTING');  
select * from categories;   

**setp7: ** 导入category_id 从1到61的数据到categories_subset_17目录 ，分隔符使用"|"，并对字符串和非字符串的null值进行转码  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--warehouse-dir=categories_subset_17 \
--where="category_id between 1 and 61" \
--fields-terminated-by="|" \
--null-string="N" \
--null-non-string="N"  

hdfs dfs -cat categories_subset_17/categories/part-*  

**setp8: ** 导入全部表结构到categories_subset_all_tables目录  
sqoop import-all-tables \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--warehouse-dir=categories_subset_all_tables   

hdfs dfs -ls categories_subset_all_tables/*  


# solution4
## 问题
**1.将表categories的子集数据导入到hive托管表，其中category_id介于1和22之**
## 指令
| 指令                | 作用                                                         |
| ------------------- | ------------------------------------------------------------ |
| --hive-import       | Import tables into Hive                                      |
## 脚本
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=categories \
--where="category_id between 1 and 22" \
--hive-import  

hive  

show tables;  
select * from tables;  

# solution 5
## 问题
**1.使用sqoop命令列出retail_db的所有表**  
**2.编写简单的sqoop eval命令以检查您是否具有读取数据库表的权限**  
**3.将所有表作为avro文件导入到/user/hive/warehouse/retail_cca174.db**  
**4.将department表作为text文件导入到/user/cloudera/departments**  
## 指令

| 指令                | 作用                                                         |
| ------------------- | ------------------------------------------------------------ |
| eval                | Overwrite existing data in the Hive table                    |
| --query             | If set, then the job will fail if the target hive.table exits. By default this property is false. |
| --compress          | Enable compression                                           |
| --compression-codec | Use Hadoop codec (default gzip)                              |
| --outdir            | Output directory for generated code                          |
## 脚本
**setp 1: **使用sqoop命令列出retail_db的所有表  
sqoop list-tables \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera

**setp 2: **编写简单的sqoop eval命令以检查您是否具有读取数据库表的权限  
sqoop eval \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--query="select count(1) from departments"

**setp 3: **将所有表作为avro文件导入到/user/hive/warehouse/retail_cca174.db  
sqoop import-all-tables \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--as-avrodatafile \
--warehouse-dir=/user/hive/warehouse/retail_cca174.db

hdfs dfs -ls /user/hive/warehouse/retail_cca174.db/*  

**setp 4: **将department表作为text文件导入到/user/cloudera/departments  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=/user/cloudera/departments  

hdfs dfs -cat /user/cloudera/departments/part-*   

# solution 6
## 问题
**1.导入整个数据库，使其可以用作hive表，必须在default schema中创建**

**2.还要确保每个表文件都在3个文件中分区**

**3.将所有Java文件存储在名为java_output的目录中，以进一步评估**  

## 指令  

| 指令                | 作用                                                         |
| ------------------- | ------------------------------------------------------------ |
| --hive-overwrite    | Overwrite existing data in the Hive table                    |
| --create-hive-table | If set, then the job will fail if the target hive.table exits. By default this property is false. |
| --compress          | Enable compression                                           |
| --compression-codec | Use Hadoop codec (default gzip)                              |
| --outdir            | Output directory for generated code                          |

## 脚本  
**setp 1: **删除所有的hive表，并检查目录  

hive  
show tables;  
drop table xxx;  
show tables;  

hdfs dfs -ls /user/hive/warehouse  

**setp 2: **导入数据库中所有表  
sqoop import-all-tables \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--hive-import \
--hive-overwrite \
--create-hive-table \
--compress \
--compression-codec=org.apache.hadoop.io.compress.SnappyCodec \
--outdir=java_output \
--m=3   

**setp 3: **验证操作结果  
hive  
show tables;  
select count(1) from categories;   

hdfs dfs -ls /user/hive/warehouse/*;

ll java_output