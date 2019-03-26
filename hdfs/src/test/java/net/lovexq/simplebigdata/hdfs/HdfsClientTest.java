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

package net.lovexq.simplebigdata.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * HDFS客户端测试类
 *
 * @author LuPindong
 * @time 2019-03-25 22:47
 */
@Slf4j
public class HdfsClientTest {

    private static FileSystem fileSystem;

    @Before
    public void getFileSystem() throws IOException {
        Configuration configuration = new Configuration();

        fileSystem = FileSystem.get(configuration);

        log.info("{}", fileSystem);
    }

    /**
     * 上传本地文件到HDFS
     * <br/>
     * -put [-f] [-p] [-l] <localsrc> ... <dst>
     *
     * @return
     * @throws IOException
     */
    @Test
    public void testUploadLocalFileToHdfs() throws IOException {
        String srcFileName = "D:\\home\\lovexq\\goldenjobs\\data\\access.log";
        String dstFileName = "/user/admin/lovexq/goldenjobs/data/access.log";
        Path dstPath = new Path(dstFileName);

        try (FileInputStream inputStream = new FileInputStream(new File(srcFileName));
             FSDataOutputStream outputStream = fileSystem.create(dstPath)) {

            IOUtils.copyBytes(inputStream, outputStream, 4096, false);
        }
    }

    /**
     * 读取HDFS文件
     * <br/>
     * -put [-f] [-p] [-l] <localsrc> ... <dst>
     *
     * @return
     * @throws IOException
     */
    @Test
    public void testReadHdfsFile() throws IOException {
        String srcFileName = "/user/admin/lovexq/goldenjobs/data/access.log";
        Path srcPath = new Path(srcFileName);

        try (FSDataInputStream inputStream = fileSystem.open(srcPath)) {
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
        }
    }
}
