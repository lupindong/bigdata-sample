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

package net.lovexq.samplebigdata.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * 我的单词计数
 * <br/>
 * 运行命令：yarn jar <jarPath> <package.className>  <input> [<input>...] <output>
 * 命令示例：yarn jar /home/lovexq/bigdata/hdfs-1.0.0-SNAPSHOT.jar net.lovexq.simplebigdata.MyWordCount \
 * /user/admin/lovexq/goldenjobs/mapreduce/input/word.txt /user/admin/lovexq/goldenjobs/mapreduce/output
 *
 * @author LuPindong
 * @time 2019-03-30 21:09
 */
public class MyWordCount {

    /**
     * Step 1 : Mapper Class
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        private static final IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if (value != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(value.toString().trim());
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    this.word.set(token);
                    context.write(this.word, one);
                }
            }
        }
    }

    /**
     * Step 2 : Reduce Class
     */
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable result = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            this.result.set(sum);
            context.write(key, this.result);
        }
    }

    /**
     * Step 3 : Main method
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1. get configuration
        Configuration configuration = new Configuration();

        String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: wordcount <in> [<in>...] <out>");
            System.exit(2);
        }

        // 2. get job instance
        Job job = Job.getInstance(configuration, MyWordCount.class.getSimpleName());

        // 3. run jar
        job.setJarByClass(MyWordCount.class);

        // 4. set job：input -> map -> shuffle -> reduce -> output
        // 4.1 input
        for (int i = 0; i < otherArgs.length - 1; i++) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        // 4.2 map
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 4.3 shuffle
        // partitioner
        //job.setPartitionerClass();
        // sort
        //job.setSortComparatorClass();
        // combiner [optional]
        job.setCombinerClass(WordCountReducer.class);
        // group
        //job.setGroupingComparatorClass();

        // 4.4 reduce
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 4.5 output
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[(otherArgs.length - 1)]));

        // 4.6 submit job
        boolean flag = job.waitForCompletion(true);
        System.exit(flag ? 0 : 1);
    }
}