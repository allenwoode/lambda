/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: BufferExample
 * Author:   feilin
 * Date:     2021/3/31 上午12:37
 * Description:
 */
package coding.buffer;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class BufferExample {

    @Test
    public void gen() throws IOException {
        Random r = new Random();
        var filename = "word";

        var bufferSize = 4*1024;
        var fout = new BufferedOutputStream(new FileOutputStream(filename), bufferSize);
        //var fout = new FileOutputStream(filename);

        var start = System.currentTimeMillis();
        for (int i=0; i<100000000; i++) {
            for (int j=0; j<5; j++) {
                fout.write(97 + r.nextInt(5));
            }
            fout.write(' ');
        }
        fout.close();
        System.out.println("cost: "+(System.currentTimeMillis() - start));
    }

    @Test
    public void test_chinese() throws UnsupportedEncodingException {
        var raw = "张以德报怨";
        var charset = StandardCharsets.UTF_8;
        System.out.println(raw.length());

//        var bytes = raw.getBytes(charset);
//        System.out.println(bytes.length);
//        System.out.println(URLEncoder.encode(raw, "utf-8"));
        var bytes = charset.encode(raw).array();
        var bytes2 = Arrays.copyOfRange(bytes, 0, 11);

        var bbuf = ByteBuffer.allocate(12);
        var cbuf = CharBuffer.allocate(12);

        bbuf.put(bytes2);
        bbuf.flip(); //读写翻转

        charset.newDecoder().decode(bbuf, cbuf, true);
        cbuf.flip();

        var aa = new char[cbuf.length()];
        if (cbuf.hasRemaining()) {
            cbuf.get(aa);
            System.out.println(">>>: "+new String(aa));
        }
        System.out.println(bbuf.limit() - bbuf.position());
        var bb = Arrays.copyOfRange(bbuf.array(), bbuf.position(), bbuf.limit());
        System.out.println(">>>: " + new String(bb));
    }

}
