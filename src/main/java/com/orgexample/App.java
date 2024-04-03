package com.orgexample;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "MmlnTask_03 started..." );

        Fraction fr = new Fraction(2,3);
        Fractionable num = Utils.cache(fr);

        System.out.println("Point 00 cached");
        System.out.println( "" );

        System.out.print("Point 01 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 02 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 03 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 04  call setNum(5), ");
        num.setNum(5);
        System.out.println("");
        System.out.print("Point 05 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 06 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 07 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 08  call num.setDenum(10), ");
        num.setDenum(10);
        System.out.println("");
        System.out.print("Point 09 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 10 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 11 ");
        System.out.println(String.format("%.2f",num.doubleValue()));

        System.out.println( "" );
        System.out.println( "MmlnTask_03 finshed..." );
    }
}
