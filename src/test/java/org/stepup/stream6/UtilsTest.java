package org.stepup.stream6;

import org.junit.Assert;
import org.junit.Test;
import org.stepup.stream6.annotations.Cache;
import org.stepup.stream6.annotations.Mutator;
import org.stepup.stream6.entities.Utils;
import org.stepup.stream6.interfaces.Fractionable;

public class UtilsTest {

    public static class FractionTst implements Fractionable {
        private int num;
        private int denum;
        public int cnt = 0;

        public FractionTst(int num, int denum) {
            this.num = num;
            this.denum = denum;
        }

        @Mutator
        public void setNum(int num) {
            this.num = num;
            System.out.print(" " + this + " ");
        }

        @Mutator
        public void setDenum(int denum) {
            this.denum = denum;
            System.out.print(" " + this + " ");
        }

        @Cache(1000)
        @Override
        public double doubleValue() {
            System.out.print(" " + this + " ");
            cnt = cnt + 1;
            return (double) num/denum;
        }

        @Override
        public String toString() {
            return "FractionTst{" +
                    "num=" + num +
                    ", denum=" + denum +
                    '}';
        }
    }

    @Test
    public void calcCache() {
        System.out.println("\nUtilsTest.cache()");
        FractionTst fr = new FractionTst(3,3);
        Fractionable nm = Utils.cache(fr);
        Assert.assertEquals(1.0,nm.doubleValue(),0);
    }

    @Test
    public void returnCache() {
        System.out.println("\nUtilsTest.cache()");
        FractionTst fr = new FractionTst(3,3);
        Fractionable nm = Utils.cache(fr);
        double res = nm.doubleValue();
        res = nm.doubleValue();
        res = nm.doubleValue();
        Assert.assertEquals( 1,fr.cnt,0);
    }
}