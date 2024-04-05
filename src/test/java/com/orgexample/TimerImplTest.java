package com.orgexample;

import junit.framework.TestCase;

public class TimerImplTest extends TestCase {

    public void testNowNano() {
        TimerImpl timer = new TimerImpl();
        Long nano = timer.nowNano();
        assertNotNull(nano.toString());
    }

    public void testNowMicro() {
        TimerImpl timer = new TimerImpl();
        Long micro = timer.nowMicro();
        assertNotNull(micro.toString());
    }

    public void testNowMilli() {
        TimerImpl timer = new TimerImpl();
        Long milli = timer.nowMilli();
        assertNotNull(milli.toString());
    }
}