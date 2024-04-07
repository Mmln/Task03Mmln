package com.orgexample;

import static java.lang.Thread.sleep;
/*
TODO Сопроводительная записка.
TODO
TODO 1. Кеш реализован при помощи ConcurrentSkipListMap. Эти коллекции не требуют дополнительной синхронизации.
TODO 2. Ключ кеша заполняется временем, отсчитываемым в микросекундах (класс TimerImpl);
TODO 3. Кеш может находиться в трех состояниях:
TODO     a) кеш пуст;
TODO     b) кеш сброшен после вызова мутатора - есть несколько строк, но самая первая имеет ключ=0 и значение=0,
TODO        т.к. после вызова мутатора необходимо добавить в кеш новое значение, то эта строка удаляется и добавляется
TODO        еще одна, с новым значением кеша (такова природа коллекции ConcurrentSkipListMap);
TODO     c) кеш в рабочем состянии - есть несколько строк, в качестве значения кеша выбирается последняя строка;
TODO 4. Параллельный процесс очистки кеша организован в классе Utils как public void Process() и перед запуском обернут
TODO    в блок синхронизации;
TODO 5. Процесс очистки кеша запускается после создания экземпляра кеша в момент вызова прокси, т.е. в самом-самом начале.
TODO 6. Внутри процесса очистки кеша крутится цикл while, прерываемый значением false переменной turnOnOffThread;
TODO 7. Процесс очистки кеша останавливается при помощи вызова Utils.setTurnOnOffThread(false) из основного модуля
TODO    в конце работы основного модуля.
TODO 8. В процессе очистки кеша подсчитывается количество его вызовов. Обычно оно равно примерно 30-40 миллионам.
TODO
 */
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

        System.out.println("\n===== Фиксируем, что кеше 3 строки, затем проверяем содержимое и... отправляемся спать на полторы секунды.");

        Utils.showCacheList("Show cacheList before sleeping(1500)");
        sleep(1500);
        System.out.println("Sleep(1500)");
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

        System.out.println("\n===== Помещаем в кеш 2 строки, затем проверяем содержимое и... отправляемся спать на полторы секунды.");
        System.out.print("Point 14  call num.setDenum(5), ");
        num.setDenum(5);
        System.out.print("Point 15 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 16 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        System.out.print("Point 17 ");
        System.out.println(String.format("%.2f",num.doubleValue()));
        Utils.showCacheList("Show cacheList before sleeping 1500");
        sleep(1500);
        System.out.println("Sleep(1500)");
        System.out.println("\n===== Выводим окончательный вариант кеша...");
        Utils.showCacheList("Show final cacheList");

        System.out.println("\n===== Фиксируем остановку процесса обработки и очистки кеша.");
        Utils.setTurnOnOffThread(false);
        System.out.println("Utils.getCountProcess()=" + Utils.getCountProcess());

        System.out.println( "" );
        System.out.println( "MmlnTask_03 finshed..." );
    }
}
