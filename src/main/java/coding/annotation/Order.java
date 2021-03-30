/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: Order
 * Author:   feilin
 * Date:     2021/3/30 上午12:04
 * Description:
 */
package coding.annotation;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Aspect(type = TimeUsageAspect.class)
public class Order implements IOrder {

    int status = 0;
    @Override
    public void pay() throws InterruptedException {
        Thread.sleep(50);
        status = 2;
    }

    @Override
    public void show() {
        System.out.println("order status is "+status);
    }

    @Test
    public void test_proxy() throws InterruptedException {
        var order = new Order();
        var proxy = (IOrder) Proxy.newProxyInstance(
                Order.class.getClassLoader(),
                new Class[]{IOrder.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before invoke method: "+method.getName());
                        return method.invoke(order);
                    }
                }
        );
        proxy.show();
        proxy.pay();
    }
}
