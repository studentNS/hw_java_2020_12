package homework;

public class App {

    public static void main(String... args) {

        TestLogging testLogging = Ioc.createTestLogging();

        testLogging.calculation(6);
        testLogging.calculation(10, 3);
        testLogging.calculation(2, 4, "Hello");
    }

}
