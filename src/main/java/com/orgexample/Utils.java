package com.orgexample;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Utils<T>  implements InvocationHandler {
    private T val;
    private Double cache = null;

    public Utils(T obj) {
        this.val = obj;
    }

    public static <T> T cache(T arg){
        return (T) Proxy.newProxyInstance(arg.getClass().getClassLoader(),
                arg.getClass().getInterfaces(),
                new Utils<>(arg));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = val.getClass().getMethod(method.getName(), method.getParameterTypes());

        Annotation[] anns = m.getDeclaredAnnotations();

        if (Arrays.stream(anns).filter(x->((Annotation)x).annotationType().equals(Mutator.class)).count()>0){
            System.out.print("Mutator called ");
            cache = null;
        }

        if (Arrays.stream(anns).filter(x->((Annotation)x).annotationType().equals(Cache.class)).count()>0) {
            if( cache == null) {
                cache = (Double) m.invoke(val,args);
                System.out.print(" doubleValue calculated and returned ");
            } else {
                System.out.print(" Cache returned ");
            }
            return cache;
        }
        return m.invoke(val, args);
    }
}
