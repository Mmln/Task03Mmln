### Описание

#### _Внимание!!! Проект нормально загружается из GitHub в IntelliJ IDEA(Community Edition)_

1.  Созданы классы Fraction, CachingHandler и Utils;
2.  Создан интерфейс Fractionable;
3.  Созданы аннотации @Chache и @Mutator;
4.  Создано необходимое количество геттеров/сеттеров;
5.  Класс UtilsOld - это вариант решения, которое я сделал и сдал в прошлом круге обучения. Сейчас оставлен для памяти;
6.  Кеш реализован при помощи ConcurrentSkipListMap. Эти коллекции не требуют дополнительной синхронизации;
7.  Ключ кеша заполняется временем, отсчитываемым в микросекундах (класс TimerImpl);
8.  Значение кеша - Object (в файле CachingHandlerHashMap лежит вариант, где в качестве значения я использовал
    HashMap<Method, Object>)
9.  Кеш может находиться в трех состояниях:
    a) кеш пуст;
    b) кеш сброшен после вызова мутатора - есть несколько строк, но самая первая имеет ключ=0 и значение=0,
    т.к. после вызова мутатора необходимо добавить в кеш новое значение, то эта строка удаляется и добавляется
    еще одна, с новым значением кеша (такова природа коллекции ConcurrentSkipListMap);
    c) кеш в рабочем состянии - есть несколько строк, в качестве значения кеша выбирается последняя строка;
10. Параллельный процесс очистки кеша организован в классе CachingHandler как public void Process() и перед
    запуском обернут в блок синхронизации;
    11.Внутри процесса очистки кеша крутится цикл while, прерываемый значением false переменной turnOnOffThread.
    Процесс очистки засыпает на 0.1 секунды;
12. Процесс очистки кеша останавливается при помощи вызова CachingHandler.setTurnOnOffThread(false) из основного модуля
    в конце работы основного модуля;
13. В процессе очистки кеша подсчитывается количество его вызовов;
14. Реализованы модульные тесты;
15. Вот протокол запуска задачи:

### _Task03Mmln started..._

#### _Фиксируем создание cacheList и запуск процесса обработки и очистки кеша._
Thread cacheProcessing stated countProcess=1  
Point 01  Fraction{num=2, denum=3}  cacheList isEmpty value calculated and returned 0.67  
Point 02  Cache returned 0.67  
Point 03  Cache returned 0.67  
Point 04  call setNum(5), Mutator called  Fraction{num=5, denum=3}  
Point 05  Fraction{num=5, denum=3}  chacheList is null value calculated and returned 1.67  
Point 06  Cache returned 1.67  
Point 07  Cache returned 1.67  
Point 08  call num.setDenum(10), Mutator called  Fraction{num=5, denum=10}  
Point 09 Fraction{num=5, denum=10}  chacheList is null value calculated and returned 0.50  

#### _Фиксируем, что кеше 3 строки, затем проверяем содержимое и... отправляемся спать на полторы секунды._
Show cacheList before sleeping(1500)  
Key=1719334398919357, Value=0.6666666666666666  
Key=1719334398940410, Value=1.6666666666666667  
Key=1719334398941621, Value=0.5  
Sleep(1500)  

#### _Проверяем содержимое кеша..._
Show cacheList after sleeping  
Key=1719334398941621, Value=0.5  

#### _Проверяем, что в кеше обновляется ключ после использования кеша ..._
Point 10  Cache returned 0.50  
Show cacheList after updating keys  
Key=1719334400444311, Value=0.5  
Point 11  Cache returned 0.50  
Show cacheList after updating keys  
Key=1719334400444955, Value=0.5  
Point 12  Cache returned 0.50  
Show cacheList after updating keys  
Key=1719334400445457, Value=0.5  
Point 13  Cache returned 0.50  
Show cacheList after updating keys  
Key=1719334400446057, Value=0.5  

#### _Помещаем в кеш 2 строки, затем проверяем содержимое и... отправляемся спать на полторы секунды._
Point 14  call num.setDenum(5), Mutator called  Fraction{num=5, denum=5}  
Point 15  Fraction{num=5, denum=5}  chacheList is null value calculated and returned 1.00  
Point 16  Cache returned 1.00  
Point 17  Cache returned 1.00  
Show cacheList before sleeping 1500  
Key=1719334400446057, Value=0.5  
Key=1719334400448618, Value=1.0  
Sleep(1500)  

#### _Выводим окончательный вариант кеша..._
Show final cacheList  
Key=1719334400448618, Value=1.0  

#### _Фиксируем остановку процесса обработки и очистки кеша._
Thread cacheProcessing finished countProcess=199579  
Utils.getCountProcess()=199579  

### _Task03Mmln finshed..._

