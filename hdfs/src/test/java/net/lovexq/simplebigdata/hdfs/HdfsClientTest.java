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
import org.apache.hadoop.fs.FileSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * HDFS客户端示例
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
     * 获取FileSystem对象
     *
     * @return
     * @throws IOException
     */
    @Test
    public void testGetFileSystem() {
        log.info("{}", fileSystem);
    }
}
