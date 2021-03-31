/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: BufferExample
 * Author:   feilin
 * Date:     2021/3/31 上午12:37
 * Description:
 */
package coding.buffer;

import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class BufferExample {

    @Test
    public void gen() throws IOException {
        Random r = new Random();
        var filename = "word";

        var bufferSize = 4*1024;
        var fout = new BufferedOutputStream(new FileOutputStream(filename), bufferSize);
        //var fout = new FileOutputStream(filename);

        var start = System.currentTimeMillis();
        for (int i=0; i<1000000; i++) {
            for (int j=0; j<5; j++) {
                fout.write(97 + r.nextInt(5));
            }
        }
        fout.close();
        System.out.println("cost: "+(System.currentTimeMillis() - start));
    }

    @Test
    public void test_chinese() {
        var raw = "张以德报怨";
        var charset = StandardCharsets.UTF_8;

    }
}
