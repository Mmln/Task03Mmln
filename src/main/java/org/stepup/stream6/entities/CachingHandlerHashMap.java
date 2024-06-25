package org.stepup.stream6.entities;

import org.stepup.stream6.annotations.Cache;
import org.stepup.stream6.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class CachingHandlerHashMap<T> implements InvocationHandler,  Runnable {
    private T val;
    private Map<Method, Object> res = new HashMap<>();
    private static ConcurrentNavigableMap<Long, Map<Method, Object>> cacheList = new ConcurrentSkipListMap<>();
    private static Long lifeTime = null;
    private static TimerImpl timer = new TimerImpl(); // this is a timer for calculationg microsecomds
    private static Boolean turnOnOffThread = false;
    private static Thread cacheProcessing = null;
    private static Long countProcess = 0L;

    public CachingHandlerHashMap(T arg) {
        this.val = arg;
        countProcess = 1L;
        turnOnOffThread = true;
        cacheProcessing = new Thread(this);
        cacheProcessing.start();
        System.out.println("Thread cacheProcessing stated countProcess=" + countProcess);
    }

    @Override
    public void run(){
        while(turnOnOffThread){
            try {
                synchronized(this){Process();}
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setTurnOnOffThread(Boolean turnOnOffThreadSwitch) {
        // Instuct cacheProcessing to finish
        CachingHandlerHashMap.turnOnOffThread = turnOnOffThreadSwitch;
        if(cacheProcessing != null) {
            try {
                // Wait for cacheProcessing to finish
                cacheProcessing.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread cacheProcessing finished countProcess=" + countProcess);
        }
    }

    public static Boolean getTurnOnOffThread() {
        return turnOnOffThread;
    }

    public static void showCacheList(String title){
        // Этот метод чисто для демонстрации, чтобы показать список строк в кеше, поэтому захардкоджен doubleValue
        System.out.println(title);
        boolean notFractionable = true;
        for (Map.Entry<Long, Map<Method, Object>> entry : cacheList.entrySet())
        {
            System.out.print("     Key=" + entry.getKey());
            // Если кешируем что-то другое, а не класс Fractionable, то выведется только Key
            for(Map.Entry<Method, Object> innerEntry : entry.getValue().entrySet()) {
                if(innerEntry.getKey().getName() ==  "doubleValue") {
                    System.out.println(", Value=" + innerEntry.getValue() + " Key=" + innerEntry.getKey().getName());
                    notFractionable = false;
                }
            }
            if(notFractionable) System.out.println(" notFractionable");
        }
    }

    public static Long getCountProcess() {
        return countProcess;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = val.getClass().getMethod(method.getName(), method.getParameterTypes());

        if(m.isAnnotationPresent(Mutator.class)) {
            System.out.print("Mutator called ");
            // Mutator called and cache DROPPED. We can't use null value in ConcurrentSkipListMap
            res.put(m, 0.0);
            CachingHandlerHashMap.cacheList.put(0L, res);
        }

        if(m.isAnnotationPresent(Cache.class)) {
            Cache anno = m.getAnnotation(Cache.class);
            if(lifeTime == null) lifeTime = (long) anno.value()*1000;
            if( CachingHandlerHashMap.cacheList.isEmpty()) {
                // Fill the empty cache
                res.put(m, m.invoke(val,args));
                CachingHandlerHashMap.cacheList.put(timer.nowMicro(), res);
                System.out.print(" cacheList isEmpty doubleValue calculated and returned ");
                if (CachingHandlerHashMap.cacheList.lastEntry().getValue().containsKey(m)) {
                    return CachingHandlerHashMap.cacheList.lastEntry().getValue().get(m);}
            }
            else if (CachingHandlerHashMap.cacheList.firstEntry().getKey() == 0L &&
                     CachingHandlerHashMap.cacheList.firstEntry().getValue().containsKey(m) &&
                     CachingHandlerHashMap.cacheList.firstEntry().getValue().containsValue(0.0)) {
                // Remove zero value for the cache and put normal value
                CachingHandlerHashMap.cacheList.remove(CachingHandlerHashMap.cacheList.firstEntry().getKey());
                res.put(m, m.invoke(val,args));
                CachingHandlerHashMap.cacheList.put(timer.nowMicro(), res );
                System.out.print(" chacheList is null doubleValue calculated and returned ");
            } else {
                // Update key/time for the carrent chache value. We can only remove and replace by key
                Map<Method, Object> value = CachingHandlerHashMap.cacheList.lastEntry().getValue();
                CachingHandlerHashMap.cacheList.remove(CachingHandlerHashMap.cacheList.lastEntry().getKey());
                CachingHandlerHashMap.cacheList.put(timer.nowMicro(), value);
                System.out.print(" Cache returned ");
            }
            return CachingHandlerHashMap.cacheList.lastEntry().getValue().get(m);
        }

        return m.invoke(val, args);

    }

    public void Process() throws InterruptedException {
        Long key;
        if(lifeTime != null) {
            Thread.sleep(10L);
            Long currentTimeInMicro = timer.nowMicro();
            for (Map.Entry<Long, Map<Method, Object>> entry : cacheList.entrySet()) {
                key = entry.getKey();
                if (key > 0 && (currentTimeInMicro - key) >= lifeTime && cacheList.size()>1) cacheList.remove(entry.getKey());
            }
        }
        countProcess += 1;
    }

}
