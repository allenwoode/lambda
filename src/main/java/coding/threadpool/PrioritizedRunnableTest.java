/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: PrioritizedRunnableTest
 * Author:   feilin
 * Date:     2021/4/1 上午12:09
 * Description:
 */
package coding.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PrioritizedRunnableTest {

    public static void main(String[] args) throws InterruptedException {
        var executor = new ThreadPoolExecutor(
                1,
                2,
                Long.MAX_VALUE,
                TimeUnit.NANOSECONDS,
                new PriorityBlockingQueue<>(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        Random r = new Random();
        List<PrioritizedRunnable> list = new ArrayList<>();
        int a = 0;
        while (a < 10) {
            //executor.execute(new PrioritizedRunnable(r.nextInt(1000), "job "+a));
            var rts = r.nextInt(500);
            var name = "job-"+a;
            list.add(new PrioritizedRunnable(rts, name));
            a++;
        }

        for (PrioritizedRunnable p : list) {
            executor.execute(p);
        }

        System.out.println("====== start =======");
        Thread.sleep(30000);
        executor.shutdown();
        System.out.println("done.");
    }
}
