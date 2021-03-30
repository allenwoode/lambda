/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: ObjectFactory
 * Author:   feilin
 * Date:     2021/3/29 下午11:44
 * Description:
 */
package coding.annotation;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;

public class ObjectFactory {

    public static <T> T newInstance(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        var annotations = clazz.getAnnotations();
        var aspects = new LinkedList<IAspect>();
        for (var annotation : annotations) {
            if (annotation instanceof Aspect) {
                var type = ((Aspect) annotation).type();
                var aspect = (IAspect) type.getConstructor().newInstance();
                aspects.add(aspect);
            }
        }

        var inst = clazz.getConstructor().newInstance();
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                clazz.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        aspects.forEach(aspect -> aspect.before());
                        var result = method.invoke(inst);
                        aspects.forEach(aspect -> aspect.after());
                        return result;
                    }
                }
        );
    }

    static PrintStream hello() {
        return System.out.format("{}", "hello");
    }

    @Test
    public void test() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        IOrder order = ObjectFactory.newInstance(Order.class);
        order.pay();
        order.show();
    }
}
