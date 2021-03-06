# Hive Guide

LanceLand <<lupindong@gmail.com>> ,v0.0.1,2019.03.30

[TOC]

# [GettingStarted](https://cwiki.apache.org/confluence/display/Hive/GettingStarted)

## Installation and Configuration
### Running Hive
#### Running HiveServer2 and Beeline
Starting from Hive 2.1, we need to run the schematool command below as an initialization step. For example, we can use "derby" as db type. 
```shell
$ $HIVE_HOME/bin/schematool -dbType <db type> -initSchema
```
HiveServer2 (introduced in Hive 0.11) has its own CLI called Beeline.  HiveCLI is now deprecated in favor of Beeline, as it lacks the multi-user, security, and other capabilities of HiveServer2.  To run HiveServer2 and Beeline from shell:
```shell
$ $HIVE_HOME/bin/hiveserver2

$ $HIVE_HOME/bin/beeline -u jdbc:hive2://$HS2_HOST:$HS2_PORT
```
Beeline is started with the JDBC URL of the HiveServer2, which depends on the address and port where HiveServer2 was started.  By default, it will be (localhost:10000), so the address will look like jdbc:hive2://localhost:10000.

Or to start Beeline and HiveServer2 in the same process for testing purpose, for a similar user experience to HiveCLI:
```shell
$ $HIVE_HOME/bin/beeline -u jdbc:hive2://
```

## DDL Operations

### Creating Hive Tables
```shell
hive> CREATE TABLE pokes (foo INT, bar STRING);
```
creates a table called pokes with two columns, the first being an integer and the other a string.
```shell
hive> CREATE TABLE invites (foo INT, bar STRING) PARTITIONED BY (ds STRING);
```
creates a table called invites with two columns and a partition column called ds. The partition column is a virtual column. It is not part of the data itself but is derived from the partition that a particular dataset is loaded into.

By default, tables are assumed to be of text input format and the delimiters are assumed to be ^A(ctrl-a).

### Browsing through Tables
```shell
hive> SHOW TABLES;
```
lists all the tables.
```shell
hive> SHOW TABLES '.*s';
```
lists all the table that end with 's'. The pattern matching follows Java regular expressions. Check out this link for documentation http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html.
```shell
hive> DESCRIBE invites;
```
shows the list of columns.

### Altering and Dropping Tables
Table names can be changed and columns can be added or replaced:
```shell
hive> ALTER TABLE events RENAME TO 3koobecaf;
hive> ALTER TABLE pokes ADD COLUMNS (new_col INT);
hive> ALTER TABLE invites ADD COLUMNS (new_col2 INT COMMENT 'a comment');
hive> ALTER TABLE invites REPLACE COLUMNS (foo INT, bar STRING, baz INT COMMENT 'baz replaces new_col2');
```
Note that REPLACE COLUMNS replaces all existing columns and only changes the table's schema, not the data. The table must use a native SerDe. REPLACE COLUMNS can also be used to drop columns from the table's schema:
```shell
hive> ALTER TABLE invites REPLACE COLUMNS (foo INT COMMENT 'only keep the first column');
```
Dropping tables:
```shell
hive> DROP TABLE pokes;
```
## DML Operations
The Hive DML operations are documented in Hive Data Manipulation Language.

Loading data from flat files into Hive:
```shell
hive> LOAD DATA LOCAL INPATH './examples/files/kv1.txt' OVERWRITE INTO TABLE pokes;
```
Loads a file that contains two columns separated by ctrl-a into pokes table. 'LOCAL' signifies that the input file is on the local file system. If 'LOCAL' is omitted then it looks for the file in HDFS.

The keyword 'OVERWRITE' signifies that existing data in the table is deleted. If the 'OVERWRITE' keyword is omitted, data files are appended to existing data sets.

NOTES:

NO verification of data against the schema is performed by the load command.
If the file is in hdfs, it is moved into the Hive-controlled file system namespace.
The root of the Hive directory is specified by the option hive.metastore.warehouse.dir in hive-default.xml. We advise users to create this directory before trying to create tables via Hive.
```shell
hive> LOAD DATA LOCAL INPATH './examples/files/kv2.txt' OVERWRITE INTO TABLE invites PARTITION (ds='2008-08-15');
hive> LOAD DATA LOCAL INPATH './examples/files/kv3.txt' OVERWRITE INTO TABLE invites PARTITION (ds='2008-08-08');
```
The two LOAD statements above load data into two different partitions of the table invites. Table invites must be created as partitioned by the key ds for this to succeed.
```shell
hive> LOAD DATA INPATH '/user/myname/kv2.txt' OVERWRITE INTO TABLE invites PARTITION (ds='2008-08-15');
```
The above command will load data from an HDFS file/directory to the table.
Note that loading data from HDFS will result in moving the file/directory. As a result, the operation is almost instantaneous.

## SQL Operations
The Hive query operations are documented in Select.

### Example Queries
Some example queries are shown below. They are available in build/dist/examples/queries.
More are available in the Hive sources at ql/src/test/queries/positive.

#### SELECTS and FILTERS
```shell
hive> SELECT a.foo FROM invites a WHERE a.ds='2008-08-15';
```
selects column 'foo' from all rows of partition ds=2008-08-15 of the invites table. The results are not stored anywhere, but are displayed on the console.

Note that in all the examples that follow, INSERT (into a Hive table, local directory or HDFS directory) is optional.

```shell
hive> INSERT OVERWRITE DIRECTORY '/tmp/hdfs_out' SELECT a.* FROM invites a WHERE a.ds='2008-08-15';
```
selects all rows from partition ds=2008-08-15 of the invites table into an HDFS directory. The result data is in files (depending on the number of mappers) in that directory.
NOTE: partition columns if any are selected by the use of *. They can also be specified in the projection clauses.

Partitioned tables must always have a partition selected in the WHERE clause of the statement.

```shell
hive> INSERT OVERWRITE LOCAL DIRECTORY '/tmp/local_out' SELECT a.* FROM pokes a;
```
selects all rows from pokes table into a local directory.
```shell
hive> INSERT OVERWRITE TABLE events SELECT a.* FROM profiles a;
hive> INSERT OVERWRITE TABLE events SELECT a.* FROM profiles a WHERE a.key < 100;
hive> INSERT OVERWRITE LOCAL DIRECTORY '/tmp/reg_3' SELECT a.* FROM events a;
hive> INSERT OVERWRITE DIRECTORY '/tmp/reg_4' select a.invites, a.pokes FROM profiles a;
hive> INSERT OVERWRITE DIRECTORY '/tmp/reg_5' SELECT COUNT(*) FROM invites a WHERE a.ds='2008-08-15';
hive> INSERT OVERWRITE DIRECTORY '/tmp/reg_5' SELECT a.foo, a.bar FROM invites a;
hive> INSERT OVERWRITE LOCAL DIRECTORY '/tmp/sum' SELECT SUM(a.pc) FROM pc1 a;
```
selects the sum of a column. The avg, min, or max can also be used. Note that for versions of Hive which don't include HIVE-287, you'll need to use COUNT(1) in place of COUNT(*).

#### GROUP BY
```shell
hive> FROM invites a INSERT OVERWRITE TABLE events SELECT a.bar, count(*) WHERE a.foo > 0 GROUP BY a.bar;
hive> INSERT OVERWRITE TABLE events SELECT a.bar, count(*) FROM invites a WHERE a.foo > 0 GROUP BY a.bar;
```
Note that for versions of Hive which don't include HIVE-287, you'll need to use COUNT(1) in place of COUNT(*).

#### JOIN
```shell
hive> FROM pokes t1 JOIN invites t2 ON (t1.bar = t2.bar) INSERT OVERWRITE TABLE events SELECT t1.bar, t1.foo, t2.foo;
```
#### MULTITABLE INSERT
```shell
FROM src
  INSERT OVERWRITE TABLE dest1 SELECT src.* WHERE src.key < 100
  INSERT OVERWRITE TABLE dest2 SELECT src.key, src.value WHERE src.key >= 100 and src.key < 200
  INSERT OVERWRITE TABLE dest3 PARTITION(ds='2008-04-08', hr='12') SELECT src.key WHERE src.key >= 200 and src.key < 300
  INSERT OVERWRITE LOCAL DIRECTORY '/tmp/dest4.out' SELECT src.value WHERE src.key >= 300;
```
#### STREAMING
```shell
hive> FROM invites a INSERT OVERWRITE TABLE events SELECT TRANSFORM(a.foo, a.bar) AS (oof, rab) USING '/bin/cat' WHERE a.ds > '2008-08-09';
```
This streams the data in the map phase through the script /bin/cat (like Hadoop streaming).
Similarly – streaming can be used on the reduce side (please see the Hive Tutorial for examples).

## Simple Example Use Cases
### MovieLens User Ratings
First, create a table with tab-delimited text file format:
```shell
hive> CREATE TABLE u_data (
  userid INT,
  movieid INT,
  rating INT,
  unixtime STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE;
```
Then, download the data files from MovieLens 100k on the GroupLens datasets page (which also has a README.txt file and index of unzipped files):
```shell
wget http://files.grouplens.org/datasets/movielens/ml-100k.zip
```
or:
```shell
curl --remote-name http://files.grouplens.org/datasets/movielens/ml-100k.zip
```
Note:  If the link to GroupLens datasets does not work, please report it on HIVE-5341 or send a message to the user@hive.apache.org mailing list.

Unzip the data files:
```shell
unzip ml-100k.zip
```
And load u.data into the table that was just created:
```shell
hive> LOAD DATA LOCAL INPATH '<path>/u.data'
OVERWRITE INTO TABLE u_data;
```
Count the number of rows in table u_data:
```shell
hive> SELECT COUNT(*) FROM u_data;
```
Note that for older versions of Hive which don't include HIVE-287, you'll need to use COUNT(1) in place of COUNT(*).

Now we can do some complex data analysis on the table u_data:

Create weekday_mapper.py:
```python
import sys
import datetime

for line in sys.stdin:
  line = line.strip()
  userid, movieid, rating, unixtime = line.split('\t')
  weekday = datetime.datetime.fromtimestamp(float(unixtime)).isoweekday()
  print '\t'.join([userid, movieid, rating, str(weekday)])
```
Use the mapper script:
```shell
hive> CREATE TABLE u_data_new (
  userid INT,
  movieid INT,
  rating INT,
  weekday INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t';

hive> add FILE weekday_mapper.py;

hive> INSERT OVERWRITE TABLE u_data_new
SELECT
  TRANSFORM (userid, movieid, rating, unixtime)
  USING 'python weekday_mapper.py'
  AS (userid, movieid, rating, weekday)
FROM u_data;

hive> SELECT weekday, COUNT(*)
FROM u_data_new
GROUP BY weekday;
```
Note that if you're using Hive 0.5.0 or earlier you will need to use COUNT(1) in place of COUNT(*).