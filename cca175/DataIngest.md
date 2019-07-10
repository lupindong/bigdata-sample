# solution1 
## 问题
**1.连接mysql db，并检查表内容**  
**2.将"retail_db.categories"表复制到hdfs，而不指定目录名**  
**3.将"retail_db.categories"表复制到hdfs，存储在目录“categories_target”中**    
**4.将"retail_db.categories"表复制到hdfs，存储在仓库目录“categories_warehouse”中**  


## 指令

| 指令                | 描述  |
| ------------------- | --------------------------------------- |
| --target-dir <dir>  | HDFS destination dir                    |
| --warehouse-dir <dir> | HDFS parent for table destination       |

## 脚本
**setp1: **连接mysql db，并检查表内容  
mysql -uretail_dba -pcloudera  retail_db;    

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

| 指令                | 描述  |
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

| 指令                | 描述  |
| ------------------- | --------------------------------------- |
| --where <where clause> | WHERE clause to use during import |
| --fields-terminated-by <char> | Sets the field separator character |
| --null-string <null-string> | The string to be written for a null value for string columns |
| --null-non-string <null-string> | The string to be written for a null value for non-string columns |
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
| 指令                | 描述                                                         |
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

| 指令                | 描述                                             |
| ------------------- | ------------------------------------------------ |
| eval                | Evaluate a SQL statement and display the results |
| --query <statement> | Import the results of *statement*                |
| --as-avrodatafile   | Imports data to Avro Data Files                  |
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

| 指令                    | 描述                                                         |
| ----------------------- | ------------------------------------------------------------ |
| --hive-overwrite        | Overwrite existing data in the Hive table                    |
| --create-hive-table     | If set, then the job will fail if the target hive.table exits. By default this property is false. |
| --compress              | Enable compression                                           |
| --compression-codec <c> | Use Hadoop codec (default gzip)                              |
| --outdir <dir>          | Output directory for generated code                          |

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

# solution 7
## 问题
**1.使用自定义boundary query导入部门表，该查询可导入1到25之间的部门**

**2.还要确保每个表文件都在2个文件中分区**

**3.还要确保只导入了两列表单表，分别是department_id、department_name**  

## 指令  

| 指令                         | 描述                                      |
| ---------------------------- | ----------------------------------------- |
| --boundary-query <statement> | Boundary query to use for creating splits |


## 脚本  
**setp 1: **  检查并删除历史记录
hdfs dfs -ls  

hdfs dfs -rm -R xxx  

**setp 2: **  使用自定义boundary query导入部门表，该查询可导入1到25之间的部门
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--columns="department_id, department_name" \
--boundary-query="select 1, 25  from departments"  \
--m=2  \
--target-dir=/user/cloudera/departments  

hdfs dfs -ls /user/cloudera/departments   

hdfs dfs -cat /user/cloudera/departments/part-*   


# solution 8
## 问题
**1.导入order表和order_items表连接的连接结果（orders.order_id = order_items.order_item_order_id）**

**2.还要确保每个表文件都在2个文件中分区**

**3.还要确保使用order_id列作为sqoop的边界条件**  

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
| --split-by <column-name> |Column of the table used to split work units. Cannot be used with `--autoreset-to-one-mapper` option. |

## 脚本  
**setp 1: **  检查并删除历史记录
hdfs dfs -ls  

hdfs dfs -rm -R xxx  

**setp 2: **  查询表结构，并查看sql执行计划
mysql -uretail_dba -pcloudera  retail_db;  

desc orders;   

desc order_items;  

explain select * from orders left join  order_items on orders.order_id = order_items.order_item_order_id;

alter table `order_items` add index `idx_oi_orderitemorderid`(`order_item_order_id`) using btree;

**setp 3: **  使用自定义boundary query导入联合查询结果
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--query="select * from orders left join  order_items on orders.order_id = order_items.order_item_order_id where \$CONDITIONS" \
--split-by=order_id \
--m=2 \
--target-dir=/user/cloudera/orders_join  

hdfs dfs -ls /user/cloudera/orders_join  


# solution 9
## 问题
**1.导入部门表到指定目录**  
**2.再次导入部门表到相同的目录（但是目录已经存在，因此它不应该覆盖，并附加结果）**  
**3.还要确保结果字段以“|”结尾 和以“\ n”结尾的行**  

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
| --lines-terminated-by <char> |Sets the end-of-line character |
| --append |Append data to an existing dataset in HDFS |

## 脚本  
**setp 1: **  检查并删除历史记录  
hdfs dfs -ls  

hdfs dfs -rm -R xxx  

**setp 2: **  首次导入部门表到指定目录  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=/user/cloudera/departments  \
--fields-terminated-by="|" \
--lines-terminated-by="\n" \
--m=1

hdfs dfs -ls /user/cloudera/departments   

**setp 3: **  第二次导入部门表到指定目录  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=/user/cloudera/departments  \
--fields-terminated-by="|" \
--lines-terminated-by="\n" \
--append \
--m=1

hdfs dfs -ls /user/cloudera/departments   



# solution 10
## 问题
**1.创建一个名为cloudera的hive数据库并在其中创建一个名为departments的表，其中包含以下字段：department_id int, department_name string**  
位置应该是：hdfs://quickstart.cloudera:8020/user/hive/warehouse/cloudera.db/departments  
**2.请导入数据到上面创建的表，从retail_db.departments导入到hive表cloudera.departments中**  
**3.请导入数据到不存在的表，这意味着在导入的同时创建名为cloudera.departments_new的hive表**  

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
| --hive-home <dir> |Override `$HIVE_HOME` |

## 脚本  

**setp 1: **  检查并删除历史记录
hdfs dfs -ls  

hdfs dfs -rm -R xxx  

**setp 2: **  创建一个名为cloudera的hive数据库并在其中创建一个名为departments的表，其中包含以下字段：department_id int, department_name string  
hive  

create database cloudera;  

use cloudera; 

show tables;  

create table departments(department_id int, department_name string);  

show tables;  

desc departments;  

**setp 3: **  请导入数据到上面创建的表，从retail_db.departments导入到hive表cloudera.departments中  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--hive-home=/user/hive/warehouse \
--hive-import \
--hive-overwrite \
--hive-table=cloudera.departments  

hive  

use cloudera;  

show tables;  

desc departments;;  

select * from departments;

**setp 4: **  请导入数据到不存在的表，这意味着在导入的同时创建名为cloudera.departments_new的hive表  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--hive-home=/user/hive/warehouse \
--hive-import \
--hive-overwrite \
--hive-table=cloudera.departments_new  \
--create-hive-table 

hive  

use cloudera;  

show tables;  

desc departments;;  

select * from departments;  


# solution 11
## 问题
**1.在名为departments的目录中导入departments表**    
**2.导入完成后，请在departments mysql表中插入以下5条记录**   
insert into departments values(10, "Physics");
insert into departments values(11, "Chemistry");
insert into departments values(12, "Maths");
insert into departments values(13, "Sciences");
insert into departments values(14, "Engineering");

**3.现在只导入新插入的记录，并附加到已存在的目录，该目录是在第一步中创建的**    

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
| --check-column (col) |Specifies the column to be examined when determining which rows to import |
| --incremental (mode) |Specifies how Sqoop determines which rows are new (eg. `append` and `lastmodified`) |
|--last-value (value) |Specifies the maximum value of the check column from the previous import |
## 脚本  

**setp 1: **  检查并删除历史记录 
hdfs dfs -ls /user/cloudera/departments  

hdfs dfs -rm -R /user/cloudera/departments  

**setp 2: **  在名为departments的目录中导入departments表 
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=/user/cloudera/departments 

hdfs dfs -ls /user/cloudera/departments  

hdfs dfs -cat /user/cloudera/departments/part-* 

**setp 3: ** 导入完成后，请在departments mysql表中插入以下5条记录 
mysql -uretail_dba -pcloudera retail_db;

insert into departments values(10, "Physics");
insert into departments values(11, "Chemistry");
insert into departments values(12, "Maths");
insert into departments values(13, "Sciences");
insert into departments values(14, "Engineering");

select * from departments; 

**setp 4: **  现在只导入新插入的记录，并附加到已存在的目录，该目录是在第一步中创建的  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=/user/cloudera/departments \
--append \
--check-column="department_id" \
--incremental=append \
--last-value=7 

hdfs dfs -ls /user/cloudera/departments  

hdfs dfs -cat /user/cloudera/departments/part-* 


# solution 12
## 问题
**1.使用以下定义在retail_db中创建一个表:**  
create table departments_new(department_id int(11), department_name varchar(45), created_date timestamp default now());
**2.现在将departments表中的记录插入departments_new**  
**3.现在将departments_new表中的数据导入到hdfs**  
**4.在departments_new表中插入以下5条记录:**  
insert into departments_new values(110, "Vivil",null);
insert into departments_new values(111, "Mechanical",null);
insert into departments_new values(112, "Automobile",null);
insert into departments_new values(113, "Pharma",null);
insert into departments_new values(114, "Social Engineering",null);
**5.现在根据create_date列执行增量导入**  

## 指令  


## 脚本  

**setp 1: **  检查并删除历史记录 
hdfs dfs -ls /user/cloudera/departments_new  

mysql -uretail_dba -pcloudera retail_db; 

show tables; 

**setp 2: ** 使用以下定义在retail_db中创建一个表 
create table departments_new(department_id int(11), department_name varchar(45), created_date timestamp default now());

**setp 3: ** 现在将departments表中的记录插入departments_new  
insert into departments_new select a.*, null from departments a;  

**setp 4: ** 现在将departments_new表中的数据导入到hdfs  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments_new \
--target-dir=/user/cloudera/departments_new \
--split-by=department_id  

hdfs dfs -ls /user/cloudera/departments_new  

hdfs dfs -cat /user/cloudera/departments_new/part-*  

**setp 5: ** 在departments_new表中插入以下5条记录  
insert into departments_new values(110, "Vivil",null);
insert into departments_new values(111, "Mechanical",null);
insert into departments_new values(112, "Automobile",null);
insert into departments_new values(113, "Pharma",null);
insert into departments_new values(114, "Social Engineering",null);

**setp 6: ** 现在根据create_date列执行增量导入  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments_new \
--target-dir=/user/cloudera/departments_new \
--split-by=department_id \
--append \
--check-column=created_date \
--incremental=lastmodified \
--last-value="2019-07-07 07:56:08" 

hdfs dfs -ls /user/cloudera/departments_new  

hdfs dfs -cat /user/cloudera/departments_new/part-*  


# solution 13
## 问题
**1.使用以下定义在retail_db中创建一个表:**  
create table departments_export(department_id int(11), department_name varchar(45), created_date timestamp default now());
**2.现在将以下目录中的数据导入departments_export表:**  
/user/cloudera/departments_new  

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
|--export-dir <dir>|HDFS source path for the export |
| --batch|Use batch mode for underlying statement execution|

## 脚本  

**setp 1: **  检查并删除历史记录  
mysql -uretail_dba -pcloudera retail_db; 

show tables; 

**setp 2: ** 使用以下定义在retail_db中创建一个表 
create table departments_export(department_id int(11), department_name varchar(45), created_date timestamp default now()); 

select * from departments_export; 

**setp 3: ** 现在将以下目录中的数据导入departments_export表
sqoop export \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments_export \
--export-dir=/user/cloudera/departments_new \
--batch 

mysql -uretail_dba -pcloudera retail_db; 

show tables; 

select * from departments_export; 


# solution 14
## 问题
**1.在本地文件系统中使用以下内容来创建名为updated_departments.csv的csv文件**  
2,fiteness
3,footwear
12,fathematics
13,fcience
14,engineering
1000,management
**2.将此csv文件上载到hdfs文件系统**  
**3.现在将此数据从hdfs导出到mysql retail_db.departments表。 在上传期间，确保只更新现有部门，并且【需要】插入新部门**  
**4.现在使用以下内容更新updated_departments.csv文件**  
2,Fiteness
3,Footwear
12,Fathematics
13,Fcience
14,Engineering
1000,Management
2000,Quality Check
**5.现在将此文件上传到hdfs**  
**6.现在将此数据从hdfs导出到mysql retail_db.departments表。 在上传期间，确保只更新现有部门，并且【不需要】插入新部门**  

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
|--update-key <col-name>|Anchor column to use for updates. Use a comma separated list of columns if there are more than one column |
| --update-mode <mode>|Specify how updates are performed when new rows are found with non-matching keys in database(eg. `updateonly` (default) and `allowinsert`)|

## 脚本  

**setp 1: **  检查并删除历史记录  
ls  update_departments.csv 

hdfs dfs -ls departments_csv 

mysql -uretail_dba -pcloudera retail_db; 

select * from departments; 

**setp 2: **  在本地文件系统中使用以下内容来创建名为updated_departments.csv的csv文件  
vim updated_departments.csv 

2,fiteness
3,footwear
12,fathematics
13,fcience
14,engineering
1000,management

:wq!

cat updated_departments.csv 

**setp 3: **  将此csv文件上载到hdfs文件系统 
hdfs dfs -mkdir departments_csv 

hdfs dfs -put updated_departments.csv departments_csv/ 

hdfs dfs -ls departments_csv/* 

hdfs dfs -cat departments_csv/updated_departments.csv 

**setp 4: **  现在将此数据从hdfs导出到mysql retail_db.departments表。 在上传期间，确保现有部门刚刚更新，并且需要插入新部门  
sqoop export \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--export-dir=departments_csv \
--batch \
--update-key=department_id \
--update-mode=allowinsert

select * from departments; 


**setp 5: **  现在使用以下内容更新updated_departments.csv文件  
vim updated_departments.csv 

2,Fiteness
3,Footwear
12,Fathematics
13,Fcience
14,Engineering
1000,Management
2000,Quality Check

:wq!

cat updated_departments.csv 

**setp 6: ** 现在将此文件上传到hdfs**  
hdfs dfs -ls departments_csv/* 

hdfs dfs -cat departments_csv/updated_departments.csv 

hdfs dfs -put -f updated_departments.csv departments_csv/ 

hdfs dfs -cat departments_csv/updated_departments.csv 

**setp 7: **  现在将此数据从hdfs导出到mysql retail_db.departments表。 在上传期间，确保现有部门刚刚更新，并且需要插入新部门  
sqoop export \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--export-dir=departments_csv \
--batch \
--update-key=department_id \
--update-mode=updateonly

select * from departments; 

# solution 15
## 问题
**1.在mysql departmetns表中请插入以下记录** 
insert into departments values(9999, "Data Science");
**2.现在有一个下游系统将处理该文件的转储。 但是，系统的设计方式只能处理文件，如果字段包含在(')符号引用中，字段的分隔符应该是(~)，并且行需要通过: (冒号)终止。** 
**3.如果数据本身包含“（双引号）而不是应该通过\** 
**4.请在departments_encloseby目录中导入departments表，文件应该能够由下游系统处理**  

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
|--enclosed-by <char>|Sets a required field enclosing character |
|--escaped-by <char>|Sets the escape character|

## 脚本  

【fields: ,】【lines: \n】【escaped-by: \】【optionally-enclosed-by: '】
**setp 1: **  检查并删除历史记录  
mysql -uretail_dba -pcloudera retail_db; 

select * from departments; 

**setp 2: **  在mysql departmetns表中请插入以下记录  
insert into departments values(9999, "Data Science");

**setp 3: **  请在departments_encloseby目录中导入departments表，文件应该能够由下游系统处理  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=departments_encloseby \
--enclosed-by="\'" \
--escaped-by="\\" \
--fields-terminated-by="~" \
--lines-terminated-by=":"  

hdfs dfs -ls departments_encloseby/*  

hdfs dfs -cat departments_encloseby/part-*   


# solution 16
## 问题
**1.在hive中创建一个表，如下所示** 
create table departments_hive(department_id int, department_name string);   
**2.现在从mysql departmetns表导入数据到这个hive表。 请确保使用下面的hive命令可以看到数据** 
select * from departments_hive; 

## 指令  


## 脚本  
**setp 1: **  检查并删除历史记录  
hive  

show tables;

**setp 2: **  现在从mysql departmetns表导入数据到这个hive表。 请确保使用下面的hive命令可以看到数据  
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--hive-home=/user/hive/warehouse \
--hive-import \
--hive-overwrite \
--hive-table=departments_hive \
--fields-terminated-by="\001" 

hdfs dfs -ls /user/hive/warehouse/departments_hive  

hdfs dfs -cat /user/hive/warehouse/departments_hive/part-*  

select * from departments_hive; 


# solution 17
## 问题
**1.在hive中创建一个表，如下所示** 
create table departments_hive01(department_id int, department_name string, avg_salary int);   
**2.使用下面的语句在mysql中创建另一个表** 
create table if not exists departments_hive01(id int, department_name varchar(45), avg_salary int); 

**3.将department表【mysql】的所有数据复制到departments_hive01表【mysql】使用** 
insert into departments_hive01 select a.*, null from departments a;
**同时插入以下数据** 
insert into departments_hive01 values(777, "Not koown", 1000);
insert into departments_hive01 values(8888, null, 1000);
insert into departments_hive01 values(666, null, 1100);

**4.现在将数据从mysql的departments_hive01表导入到这个hive表中。 请确保在hive命令下面显示数据。 另外，在导入时，如果找到departments_name列的空值，则将其替换为“”（空字符串），将int列替换为-999** 
select * from departments_hive01; 

## 指令  


## 脚本  
**setp 1: **  检查并删除历史记录  
hive  

show tables;

mysql -uretail_dba -pcloudera retail_db;

show tables;

**setp 2: **  在hive中创建一个表，如下所示  
create table departments_hive01(department_id int, department_name string, avg_salary int); 

show tables;

**setp 3: **  使用下面的语句在mysql中创建另一个表  
create table if not exists departments_hive01(id int, department_name varchar(45), avg_salary int); 

show tables;

**setp 4: **  将department表【mysql】的所有数据复制到departments_hive01表【mysql】使用，同时插入以下数据  
select * from departments_hive01;

insert into departments_hive01 select a.*, null from departments a;

insert into departments_hive01 values(777, "Not koown", 1000);
insert into departments_hive01 values(8888, null, 1000);
insert into departments_hive01 values(666, null, 1100);

select * from departments_hive01;

**setp 5: **  现在将数据从mysql的departments_hive01表导入到这个hive表中。 请确保在hive命令下面显示数据。 另外，在导入时，如果找到departments_name列的空值，则将其替换为“”（空字符串），将id替换为-999 
sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments_hive01 \
--hive-home=/user/hive/warehouse \
--hive-import \
--hive-overwrite \
--hive-table=departments_hive01 \
--fields-terminated-by="\001" \
--null-string="" \
--null-non-string=-999 \
--split-by="id"

hdfs dfs -ls /user/hive/warehouse/departments_hive01  

hdfs dfs -cat /user/hive/warehouse/departments_hive01/part-*  

select * from departments_hive01; 


# solution 18
## 问题
**1.创建mysql表如下:** 
create table if not exists departments_hive02(id int, department_name varchar(45), avg_salary int); 

**2.现在从hive表departments_hive01导出数据到mysql表departments_hive02。在导出时，请注意以下内容:** 
只要有空字符串，它应该在mysql中作为空值加载 
只要int字段的值为-999，就应该将其创建为空值 

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
|--input-fields-terminated-by <char>| Sets the input field separator       |
|--input-lines-terminated-by <char>|Sets the input end-of-line character|

## 脚本  
**setp 1: **  检查并删除历史记录  
hive  

show tables;

mysql -uretail_dba -pcloudera retail_db;

show tables;

**setp 2: **  创建mysql表  
create table if not exists departments_hive02(id int, department_name varchar(45), avg_salary int); 

show tables;

select * from departments_hive02;

**setp 3: **  现在从hive表departments_hive01导出数据到mysql表departments_hive02
sqoop export \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments_hive02 \
--export-dir=/user/hive/warehouse/departments_hive01 \
--input-fields-terminated-by="\001" \
--input-lines-terminated-by="\n" \
--input-null-string="" \
--input-null-non-string=-999 \
--batch 

select * from departments_hive02;


# solution 19
## 问题
**1.将departments表从mysql导入hdfs，以textfile格式存储到departments_text目录** 
**2.将departments表从mysql导入hdfs，以sequncefile格式存储到departments_sequence目录** 
**3.将departments表从mysql导入hdfs，以avrofile格式存储到departments_avro目录** 
**4.将departments表从mysql导入hdfs，以parquetfile格式存储到departments_parquet目录** 

## 指令  

| 指令               | 描述                                      |
| ------------------ | ----------------------------------------- |
|--input-fields-terminated-by <char>| Sets the input field separator       |
|--input-lines-terminated-by <char>|Sets the input end-of-line character|

## 脚本  
**setp 1: **  将departments表从mysql导入hdfs，以textfile格式存储到departments_text目录  
hdfs dfs -ls  departments_text

sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=departments_text \
--as-textfile 

hdfs dfs -ls  departments_text 

hdfs dfs -cat departments_text/part-* 

**setp 2: **  将departments表从mysql导入hdfs，以sequencefile格式存储到departments_sequence目录  
hdfs dfs -ls  departments_sequence

sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=departments_sequence \
--as-sequencefile 

hdfs dfs -ls departments_sequence 

hdfs dfs -cat departments_sequence/part-* 

**setp 3: **  将departments表从mysql导入hdfs，以avrofile格式存储到departments_avro目录  
hdfs dfs -ls  departments_avro

sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=departments_avro \
--as-avrodatafile 

hdfs dfs -ls departments_avro 

hdfs dfs -cat departments_avro/part-* 

**setp 4: **  将departments表从mysql导入hdfs，以parquetfile格式存储到departments_parquet目录  
hdfs dfs -ls  departments_parquet

sqoop import \
--connect=jdbc:mysql://quickstart:3306/retail_db \
--username=retail_dba \
--password=cloudera \
--table=departments \
--target-dir=departments_parquet \
--as-parquetfile 

hdfs dfs -ls departments_parquet 

hdfs dfs -cat departments_parquet/*.parquet 