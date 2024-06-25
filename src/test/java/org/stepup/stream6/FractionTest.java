package org.stepup.stream6;

import junit.framework.TestCase;
import org.junit.Test;
import org.stepup.stream6.entities.Fraction;

public class FractionTest extends TestCase {

    @Test
    public void testSetNum() {
        Fraction fr = new Fraction(3,4);
        fr.setNum(4);
        assertEquals(fr.doubleValue(),(4.0/4.0),0.0);
    }

    @Test
    public void testSetDenum() {
        Fraction fr = new Fraction(3,4);
        fr.setDenum(3);
        assertEquals(fr.doubleValue(),(3.0/3.0),0.0);
    }

    @Test
    public void testDoublValue() {
        Fraction fr = new Fraction(3,3);
        assertEquals(fr.doubleValue(),(3.0/3.0),0.0);
    }

    @Test
    public void testTestToString() {
        Fraction fr = new Fraction(3,3);
        assertNotNull(fr.toString());
    }
}