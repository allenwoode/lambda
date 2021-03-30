/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: coding.annotation.Aspect
 * Author:   feilin
 * Date:     2021/3/29 下午11:18
 * Description:
 */
package coding.proxy;

import basic.monad.Try;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface Aspect {
    void before();
    void after();

    static <T> T getProxy(Class<T> cls, String ... aspects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var aspectInsts = Arrays.stream(aspects).map(name -> Try.ofFailable(() -> {
            var clazz = Class.forName(name);
            return  (Aspect) clazz.getConstructor().newInstance();
        }))
                .filter(aspect -> aspect.isSuccess())
                .collect(Collectors.toList());

        var inst = cls.getConstructor().newInstance();
        return (T) Proxy.newProxyInstance(
                cls.getClassLoader(),
                cls.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        for (var aspect : aspectInsts) {
                            aspect.get().before();
                        }
                        var result = method.invoke(inst);
                        for (var aspect : aspectInsts) {
                            aspect.get().after();
                        }
                        return result;
                    }
                }
        );
    }
}
