package org.stepup.stream6.entities;

import org.stepup.stream6.annotations.Cache;
import org.stepup.stream6.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Utils<T> {
    public static <T> T cache(T arg) {
        return (T) Proxy.newProxyInstance(arg.getClass().getClassLoader(),
                arg.getClass().getInterfaces(),
                new CachingHandler<>(arg));
    }
}
