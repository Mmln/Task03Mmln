package com.orgexample;

import java.math.BigDecimal;

public class TimerImpl {

    private final long offset;
    private final int countTimerImpl = 500;
    private static final int oneMillion = 1_000_000;
    private static final int oneThousand = 1_000;


    private static long calculateOffset() {
        final long nano = System.nanoTime();
        final long nanoFromMilli = System.currentTimeMillis() * oneMillion;
        return nanoFromMilli - nano;
    }

    public TimerImpl() {
        final int count = countTimerImpl;
        BigDecimal offsetSum = BigDecimal.ZERO;
        for (int i = 0; i < count; i++) {
            offsetSum = offsetSum.add(BigDecimal.valueOf(calculateOffset()));
        }
        offset = (offsetSum.divide(BigDecimal.valueOf(count))).longValue();
    }

    public long nowNano() {
        return offset + System.nanoTime();
    }

    public long nowMicro() {
        return (offset + System.nanoTime()) / oneThousand;
    }

    public long nowMilli() {
        return System.currentTimeMillis();
    }
}
