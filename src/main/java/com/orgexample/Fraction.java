package com.orgexample;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
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

    @Cache
    @Override
    public double doubleValue() {
        System.out.print(" " + this + " ");
        return (double) num/denum;
    }

    @Override
    public String toString() {
        return "Fraction{" +
                "num=" + num +
                ", denum=" + denum +
                '}';
    }
}
