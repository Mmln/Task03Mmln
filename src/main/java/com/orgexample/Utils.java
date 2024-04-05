package com.orgexample;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.lang.Thread.sleep;

public class Utils<T> implements InvocationHandler {
    private T val;
    private static ConcurrentNavigableMap<Long, Double> cacheList = new ConcurrentSkipListMap<>();
    private static Long lifeTime = null;
    private static TimerImpl timer = new TimerImpl(); // this is a timer for calculationg microsecomds
    private static Boolean turnOnOffThread = false;
    private static Thread cacheProcessing = null;
    private static Long countProcess = 0L;

    public Utils(T obj) {
        this.val = obj;
        countProcess = 1L;
        turnOnOffThread = true;
        cacheProcessing = new Thread(() ->
        {
            while(turnOnOffThread){
                try {
                    Process();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cacheProcessing.start();
        System.out.println("Thread cacheProcessing stated countProcess=" + countProcess);
    }

    public static void setTurnOnOffThread(Boolean turnOnOffThread) {
        Utils.turnOnOffThread = turnOnOffThread;
        // Wait for cacheProcessing to finish
        if(cacheProcessing != null) {
            try {
                cacheProcessing.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread cacheProcessing finished countProcess=" + countProcess);
        }
    }

    public static <T> T cache(T arg){
        return (T) Proxy.newProxyInstance(arg.getClass().getClassLoader(),
                arg.getClass().getInterfaces(),
                new Utils<>(arg));
    }

    public static Boolean getTurnOnOffThread() {
        return turnOnOffThread;
    }

    public static Object showCacheList(String title){
        System.out.println(title);
        for (Map.Entry<Long, Double> entry : cacheList.entrySet())
        {
            System.out.println("     Key=" + entry.getKey()  + ", Value=" + String.format("%.2f",entry.getValue()));
        }
        return null;
    }

    public void Utils() {
    }

    public static Long getCountProcess() {
        return countProcess;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = val.getClass().getMethod(method.getName(), method.getParameterTypes());

        Annotation[] anns = m.getDeclaredAnnotations();

        if(m.isAnnotationPresent(Mutator.class)) {
            System.out.print("Mutator called ");
            // Mutator called and cache DROPPED. We can't use null value in ConcurrentSkipListMap
            Utils.cacheList.put(0L, 0.0);
        }

        if(m.isAnnotationPresent(Cache.class)) {
            Cache anno = m.getAnnotation(Cache.class);
            if(lifeTime == null) lifeTime = (long) anno.value()*1000;
            if( Utils.cacheList.isEmpty()) {
                // Firts fill the empty cache
                Utils.cacheList.put(timer.nowMicro(), (Double) m.invoke(val,args));
                System.out.print(" cacheList isEmpty doubleValue calculated and returned ");
            }
            else if( Utils.cacheList.firstEntry().getKey() == 0L &&  Utils.cacheList.firstEntry().getValue() == 0.0){
                // Remove zero value for the cache and put normal value
                Utils.cacheList.remove(Utils.cacheList.firstEntry().getKey());
                Utils.cacheList.put(timer.nowMicro(), (Double) m.invoke(val,args));
                System.out.print(" chacheList is null doubleValue calculated and returned ");
            }
            else {
                // Update key/time for the carrent chache value. We can only remove and replace by key
                Double value = Utils.cacheList.lastEntry().getValue();
                Utils.cacheList.remove(Utils.cacheList.lastEntry().getKey());
                Utils.cacheList.put(timer.nowMicro(), value);
                System.out.print(" Cache returned ");
            }
            return Utils.cacheList.lastEntry().getValue();
        }

        return m.invoke(val, args);

    }

    public static void Process() throws InterruptedException {
        Long key = null;
        //if (countProcess % 100000000 == 0) System.out.println("countProcess=" + countProcess + " lifeTime=" + lifeTime + " cacheList.size()=" + cacheList.size());
        //sleep(5);
        //System.out.println("Process lifeTime=" + lifeTime + " cacheList.size()=" + cacheList.size());
        if(lifeTime != null) {
            Long currentTimeInMicro = timer.nowMicro();
            for (Map.Entry<Long, Double> entry : cacheList.entrySet()) {
                key = entry.getKey();
                if (key > 0 && (currentTimeInMicro - key) >= lifeTime && cacheList.size()>1) cacheList.remove(entry.getKey());
            }
        }
        countProcess += 1;
    }
}
