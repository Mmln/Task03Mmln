package org.stepup.stream6;

import junit.framework.TestCase;
import org.junit.Test;
import org.stepup.stream6.entities.TimerImpl;

public class TimerImplTest extends TestCase {

    @Test
    public void testNowNano() {
        TimerImpl timer = new TimerImpl();
        Long nano = timer.nowNano();
        assertNotNull(nano.toString());
    }

    @Test
    public void testNowMicro() {
        TimerImpl timer = new TimerImpl();
        Long micro = timer.nowMicro();
        assertNotNull(micro.toString());
    }

    @Test
    public void testNowMilli() {
        TimerImpl timer = new TimerImpl();
        Long milli = timer.nowMilli();
        assertNotNull(milli.toString());
    }
}
