/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: TimeUsageAspect
 * Author:   feilin
 * Date:     2021/3/29 下午11:32
 * Description:
 */
package coding.proxy;

public class TimeUsageAspect implements Aspect {

    long start;
    @Override
    public void before() {
        start = System.currentTimeMillis();
    }

    @Override
    public void after() {
        start = System.currentTimeMillis() - start;
        //System.out.println("use: " + start + "ms");
        System.out.format("Time usage: %dms\n", start);
    }
}
