package bytecode;


public class Real {

    public static void main(String[] args) {

        TestLoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation(2);
        myClass.calculation(3, 2);
        myClass.calculation(3, "asd");
        myClass.calculation("aasd", "asd");

    }
}
