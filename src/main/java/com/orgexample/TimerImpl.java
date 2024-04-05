package com.orgexample;

import java.math.BigDecimal;

public class TimerImpl {

    private final long offset;

    private static long calculateOffset() {
        final long nano = System.nanoTime();
        final long nanoFromMilli = System.currentTimeMillis() * 1_000_000;
        return nanoFromMilli - nano;
    }

    public TimerImpl() {
        final int count = 500;
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
        return (offset + System.nanoTime()) / 1000;
    }

    public long nowMilli() {
        return System.currentTimeMillis();
    }
}
