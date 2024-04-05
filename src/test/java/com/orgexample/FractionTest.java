package com.orgexample;

import junit.framework.TestCase;

public class FractionTest extends TestCase {

    @org.junit.Test
    public void testSetNum() {
        Fraction fr = new Fraction(3,4);
        fr.setNum(4);
        assertEquals(fr.doubleValue(),(4.0/4.0),0.0);
    }

    @org.junit.Test
    public void testSetDenum() {
        Fraction fr = new Fraction(3,4);
        fr.setDenum(3);
        assertEquals(fr.doubleValue(),(3.0/3.0),0.0);
    }

    @org.junit.Test
    public void testDoublValue() {
        Fraction fr = new Fraction(3,3);
        assertEquals(fr.doubleValue(),(3.0/3.0),0.0);
    }

    @org.junit.Test
    public void testTestToString() {
        Fraction fr = new Fraction(3,3);
        assertNotNull(fr.toString());
    }
}