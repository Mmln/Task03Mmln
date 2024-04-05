package com.orgexample;

import static java.lang.Thread.sleep;

public class App
{
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "MmlnTask_03 started..." );

        Fraction fr = new Fraction(2,3);
        System.out.println("\n===== Фиксируем создание cacheList и запуск процесса обработки и очистки кеша.");
        Fractionable num = Utils.cache(fr);
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
        System.out.print("Point 09");
        System.out.println(String.format("%.2f",num.doubleValue()));

        System.out.println("\n===== Фиксируем, что кеше 3 строки, затем проверяем содержимое и... отправляемся спать на секунду.");

        Utils.showCacheList("Show cacheList before sleeping(1000)");
        sleep(1000);
        System.out.println("Sleep(1000)");
        System.out.println("\n===== Проверяем содержимое кеша...");
        Utils.showCacheList("Show cacheList after sleeping ");

        System.out.println("\n===== Проверяем, что в кеше обновляется ключ после использования кеша ...");
        System.out.print("Point 10 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        Utils.showCacheList("Show cacheList after updating keys");
        System.out.print("Point 11 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        Utils.showCacheList("Show cacheList after updating keys");
        System.out.print("Point 12 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        Utils.showCacheList("Show cacheList after updating keys");
        System.out.print("Point 13 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        Utils.showCacheList("Show cacheList after updating keys");

        System.out.println("\n===== Помещаем в кеш 2 строки, затем проверяем содержимое и... отправляемся спать на секунду.");
        System.out.print("Point 14  call num.setDenum(5), ");
        num.setDenum(5);
        System.out.print("Point 15 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 16 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 17 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        Utils.showCacheList("Show cacheList before sleeping 1000");
        sleep(1000);
        System.out.println("Sleep(1000)");
        System.out.println("\n===== Выводим окончательный вариант кеша...");
        Utils.showCacheList("Show final cacheList");

        System.out.println("\n===== Фиксируем остановку процесса обработки и очистки кеша.");
        Utils.setTurnOnOffThread(false);
        System.out.println("Utils.getCountProcess()=" + Utils.getCountProcess());

        System.out.println( "" );
        System.out.println( "MmlnTask_03 finshed..." );
    }
}
