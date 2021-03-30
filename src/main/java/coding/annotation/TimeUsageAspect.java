/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: TimeUsageAspect
 * Author:   feilin
 * Date:     2021/3/30 上午12:05
 * Description:
 */
package coding.annotation;

public class TimeUsageAspect implements IAspect {

    long start;
    @Override
    public void before() {
        start = System.currentTimeMillis();
    }

    @Override
    public void after() {
        var usage = System.currentTimeMillis() - start;
        System.out.format("Time use: %dms\n", usage);
    }
}
