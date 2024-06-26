package org.stepup.stream6.entities;

import org.stepup.stream6.annotations.Cache;
import org.stepup.stream6.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class CachingHandler <T> implements InvocationHandler,  Runnable {
    private T val;
    private static ConcurrentNavigableMap<Long, Object> cacheList = new ConcurrentSkipListMap<>();
    private static Long lifeTime = null;
    private static TimerImpl timer = new TimerImpl(); // this is a timer for calculationg microsecomds
    private static Boolean turnOnOffThread = false;
    private static Thread cacheProcessing = null;
    private static Long countProcess = 0L;

    public CachingHandler(T arg) {
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
                synchronized(this){
                    Process();
                    cacheProcessing.sleep(10L);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setTurnOnOffThread(Boolean turnOnOffThreadSwitch) {
        // Instuct cacheProcessing to finish
        CachingHandler.turnOnOffThread = turnOnOffThreadSwitch;
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
        // This method is purely for demonstration purposes to show a list of cached strings
        System.out.println(title);
        boolean notFractionable = true;
        for (Map.Entry<Long, Object> entry : cacheList.entrySet()) {
            System.out.println("     Key=" + entry.getKey() + ", Value=" + entry.getValue());
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
            CachingHandler.cacheList.put(0L, 0.0);
        }

        if(m.isAnnotationPresent(Cache.class)) {
            Cache anno = m.getAnnotation(Cache.class);
            if(lifeTime == null) lifeTime = (long) anno.value()*1000;
            if( CachingHandler.cacheList.isEmpty()) {
                // Fill the empty cache
                CachingHandler.cacheList.put(timer.nowMicro(), m.invoke(val,args));
                System.out.print(" cacheList isEmpty value calculated and returned ");
            }
            else if (CachingHandler.cacheList.firstEntry().getKey() == 0L ||
                    CachingHandler.cacheList.firstEntry().getValue().equals(0.0)) {
                // Remove zero value for the cache and put normal value
                CachingHandler.cacheList.remove(CachingHandler.cacheList.firstEntry().getKey());
                CachingHandler.cacheList.put(timer.nowMicro(), m.invoke(val,args) );
                System.out.print(" chacheList is null value calculated and returned ");
            } else {
                // Update key/time for the carrent chache value. We can only remove and replace by key
                Object value = CachingHandler.cacheList.lastEntry().getValue();
                CachingHandler.cacheList.remove(CachingHandler.cacheList.lastEntry().getKey());
                CachingHandler.cacheList.put(timer.nowMicro(), value);
                System.out.print(" Cache returned ");
            }
            return CachingHandler.cacheList.lastEntry().getValue();
        }

        return m.invoke(val, args);

    }

    public void Process() throws InterruptedException {
        Long key;
        if(lifeTime != null) {
            Long currentTimeInMicro = timer.nowMicro();
            for (Map.Entry<Long, Object> entry : cacheList.entrySet()) {
                key = entry.getKey();
                if (key > 0 && (currentTimeInMicro - key) >= lifeTime && cacheList.size()>1) cacheList.remove(entry.getKey());
            }
        }
        countProcess += 1;
    }
}
