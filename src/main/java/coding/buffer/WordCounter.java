/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: WordCounter
 * Author:   feilin
 * Date:     2021/4/2 下午11:34
 * Description:
 */
package coding.buffer;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class WordCounter {

    class CountTask implements Callable<HashMap<String, Integer>> {
        private final long start;
        private final long end;
        private final String filename;

        public CountTask(String filename, long start, long end) {
            this.filename = filename;
            this.start = start;
            this.end = end;
        }

        @Override
        public HashMap<String, Integer> call() throws Exception {
            var channel = new RandomAccessFile(this.filename, "rw").getChannel();
            var mbuf = channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    this.start,
                    this.end - this.start
            );
            var str = StandardCharsets.US_ASCII.decode(mbuf).toString();
            return countByString(str);
        }
    }

    final ForkJoinPool pool = new ForkJoinPool();

    public void run(String filename, long chunkSize) throws ExecutionException, InterruptedException {
        var file = new File(filename);
        var fileSize = file.length();
        long pos = 0;
        var tasks = new ArrayList<Future<HashMap<String,Integer>>>();

        var start = System.currentTimeMillis();
        while (pos < fileSize) {
            var next = Math.min(pos+chunkSize, fileSize);
            var task = new CountTask(filename, pos, next);
            pos = next;
            var future = pool.submit(task);
            tasks.add(future);
        }

        var total = new HashMap<String, Integer>();
        for (var future : tasks) {
            var map = future.get();
            for (var entry : map.entrySet()) {
                incKey(entry.getKey(), total, entry.getValue());
            }
        }

        System.out.println("cost: " + (System.currentTimeMillis() - start)+"ms");
        System.out.println("total: " + total.size());
        System.out.println("bbb: " + total.get("bbb"));
    }


    @Test
    public void compare_with_single() throws IOException {
        var in = new BufferedInputStream(new FileInputStream("word"));
        var buf = new byte[4*1024];
        var len = 0;
        var total = new HashMap<String, Integer>();

        var start = System.currentTimeMillis();
        while ((len = in.read(buf)) != -1) {
            var bytes = Arrays.copyOfRange(buf, 0, len);
            var str = new String(bytes);
            var hashMap = countByString(str);
            for (var entry : hashMap.entrySet()) {
                var key = entry.getKey();
                incKey(key, total, entry.getValue());
            }
        }
        System.out.println("cost: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("total: "+total.size());
        System.out.println("bbb: " + total.get("bbb"));
    }

    private void incKey(String key, HashMap<String, Integer> total, Integer n) {
        if (total.containsKey(key)) {
            total.put(key, total.get(key) + n);
        } else {
            total.put(key, n);
        }
    }

    private HashMap<String, Integer> countByString(String str) {
        var map = new HashMap<String, Integer>();
        StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreElements()) {
            var word = tokenizer.nextToken();
            incKey(word, map, 1);
        }
        return map;
    }

    @Test
    public void count() throws ExecutionException, InterruptedException {
        WordCounter wc = new WordCounter();
        System.out.println("processors: " + Runtime.getRuntime().availableProcessors());
        wc.run("word", 1024*1024);
        /*
        single thead task
        cost: 20603ms
        total: 3905
        bbb: 359
         */
    }
}
