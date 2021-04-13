package homework;

public class App {

    public static void main(String... args) {

        TestRun testRun = new TestRun<TestClass>();
        testRun.runAllTests(TestClass.class);
    }
}
