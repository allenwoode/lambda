/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: PrioritizedRunnable
 * Author:   feilin
 * Date:     2021/3/31 下午11:50
 * Description:
 */
package coding.threadpool;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PrioritizedRunnable implements Runnable, Comparable<PrioritizedRunnable> {

    private long rts; // runtime
    private String name; //thread name

    //public PrioritizedRunnable() {}

    public PrioritizedRunnable(long rts, String name) {
        this.rts = rts;
        this.name = name;
    }

    public long getRts() {
        return rts;
    }

    public void setRts(long rts) {
        this.rts = rts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(PrioritizedRunnable o) {
        if (rts < o.getRts()) {
            return -1;
        } else if (rts > o.getRts()) {
            return 1;
        }
        return 0;
    }

    @Override
    public void run() {
        //Random r = new Random();

        try {
            //int sleepRandom = r.nextInt(2000);
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("rts: "+ rts + ", name: "+name);
    }

    @Test
    public void test_prioritized_thread_pool() throws InterruptedException {
        var executor = new ThreadPoolExecutor(
                2,
                2,
                Long.MAX_VALUE,
                TimeUnit.NANOSECONDS,
                new PriorityBlockingQueue<>(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        var p1 = new PrioritizedRunnable(1000, "任务A");
        var p2 = new PrioritizedRunnable(3000, "任务B");
        var p3 = new PrioritizedRunnable(3000, "任务C");
        var p4 = new PrioritizedRunnable(4000, "任务D");
        var p5 = new PrioritizedRunnable(4000, "任务E");

        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.execute(p4);
        executor.execute(p5);
        System.out.println("====== start =======");
        Thread.sleep(30000);
        executor.shutdown();
        System.out.println("done.");
    }
}
