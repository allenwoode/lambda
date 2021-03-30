/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: ProxyExampleTest
 * Author:   feilin
 * Date:     2021/3/29 下午10:57
 * Description:
 */
package coding.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class ProxyExampleTest {
    @Test
    public void test() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        IOrder order = Aspect.getProxy(Order.class,
                "coding.proxy.TimeUsageAspect");
        order.pay();
        order.show();
    }
}
