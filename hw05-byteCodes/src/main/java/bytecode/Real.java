package bytecode;


public class Real {

    public static void main(String[] args) {

        TestLoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation(2);
        myClass.calculation(3, 2);
        myClass.calculation(3);
//        myClass.calculation(3, "sdfsd");

        /**
         * Из задания не понятно, аннотацию Log устанавливать руками или кодом? Если опираться на то, что рекомендовалось использовать на занятии - Dynamic Proxy, то руками.
         * Чтобы работал этот вариант 'new TestLogging().calculation(6);', из д/з, надо класс модифицировать при компиляции или использовать javaagent?
         * Честно не знаю что можно оптимизировать в методе invoke().
         */

        new TestLogging().calculation(3);
        new TestLogging().calculation(3, 2);
        new TestLogging().calculation(3, "ё маё");
    }
}
