# solution29 
## 问题
**请使用HDFS命令行选项进行以下练习：**  
**1.在hdfs中创建一个名为hdfs_commands的目录。**  
**2.在hdfs中的hdfs_commands目录中的创建一个名为data.txt的文件。**  
**3.现在将此data.txt文件复制到本地文件系统上，但是在复制文件时请确保不更改属性，例如 文件权限。**  
**4.现在在本地目录中创建一个名称为data_local.txt文件，并将此文件移动到hdfs中的hdfs_commands目录。**  
**5.在hdfs_commands目录中创建文件data_hdfs.txt并将其复制到本地文件系统。**  
**6.在本地文件系统中创建一个名为file1.txt的文件并将其放入hdfs。**  

## 脚本
**setp1:在hdfs中创建一个名为hdfs_commands的目录。** 
hdfs dfs -mkdir -p hdfs_commands;

**setp2:在hdfs中的hdfs_commands目录中的创建一个名为data.txt的文件。**  
hdfs dfs -touchz hdfs_commands/data.txt;

**setp3.现在将此data.txt文件复制到本地文件系统上，但是在复制文件时请确保不更改属性，例如 文件权限。**  
hdfs dfs -copyToLocal -p hdfs_commands/data.txt /home/cloudera/solution29;

**setp4.现在在本地目录中创建一个名称为data_local.txt文件，并将此文件移动到hdfs中的hdfs_commands目录。**  
touch /home/cloudera/solution29/data_local.txt;

hdfs dfs -moveFromLocal /home/cloudera/solution29/data_local.txt hdfs_commands/;

**setp5.在hdfs_commands目录中创建文件data_hdfs.txt并将其复制到本地文件系统。**  
hdfs dfs -touchz hdfs_commands/data_hdfs.txt;

hdfs dfs -get -p hdfs_commands/data_hdfs.txt /home/cloudera/solution29;

**setp6.在本地文件系统中创建一个名为file1.txt的文件并将其放入hdfs。** 

touch /home/cloudera/solution29/file1.txt;

hdfs dfs -put /home/cloudera/solution29/file1.txt hdfs_commands/;  

# solution30
## 问题
**在hdfs中给出了三个csv文件，如下所示。** 
**EmployeeName.csv字段为(id,name)** 
**EmployeeManager.csv字段为(id,managerName)** 
**EmployeeSalary.csv字段为(id,salary)** 
**使用spark及其API，您必须生成一个如下所示的联合输出，并另存为一个文本文件（用逗号分隔），用于最终分发，输出必须按ID排序。** 
**id,name,salary,managerName** 

## 脚本
val nameRDD = sc.textFile("solution30/EmployeeName.csv").map(_.split(",")).map(e=>(e(0),e(1)))

val managerRDD = sc.textFile("solution30/EmployeeManager.csv").map(_.split(",")).map(e=>(e(0),e(1)))

val salaryRDD = sc.textFile("solution30/EmployeeSalary.csv").map(_.split(",")).map(e=>(e(0),e(1).toInt))

val result = nameRDD.join(salaryRDD).join(managerRDD).sortByKey().map(e=>(e._1, e._2._1._1, e._2._1._2, e._2._2))

result.saveAsTextFile("solution30/result")

# solution31
## 问题
**1. Content.txt: 包含一个包含空格分隔词的大型文本文件**  
**2. Remove.txt: 忽略/筛选此文件中给定的所有单词（逗号分隔）**  
**编写一个spark程序，读取Context.txt文件并作为RDD加载，从广播可用文件中删除所有单词（从Remove.txt加载为单词的RDD）。并计算每个单词的出现次数，并将其保存为HDFS中的文本文件。**  

## 脚本  
val contentRDD = sc.textFile("solution31/Content.txt").filter(e => e!=null && !e.isEmpty).flatMap(_.split(" ")).map(_.trim)

val removeRDD = sc.textFile("solution31/Remove.txt").filter(e => e!=null && !e.isEmpty).flatMap(_.split(",")).map(_.trim)

val bcList = sc.broadcast(removeRDD.collect.toList)

val result = contentRDD.filter{case(e) => !bcList.value.contains(e)}.map(e => (e,1)).reduceByKey(_+_)

result.saveAsTextFile("solution31/result")


# solution32
val rdd = sc.textFile("solution32/sparkdir1/file1.txt,solution32/sparkdir2/file2.txt,solution32/sparkdir3/file3.txt").flatMap(_.split(" ")).map(_.trim)

val filterWordsRDD = sc.parallelize(List("a","the","an","as","a","with","this","there","is","are","in","for","to","and","The","of"))

val filteredRDD = rdd.subtract(filterWordsRDD)

val result = filteredRDD.map(e => (e,1)).reduceByKey(_+_).sortByKey(false)

















