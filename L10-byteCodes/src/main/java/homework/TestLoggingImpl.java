package homework;

import homework.annotations.Log;

public class TestLoggingImpl implements TestLogging {

    @Log
    @Override
    public void calculation(int param) {
        int result = param;
        System.out.println(result);
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        int result = param1 + param2;
        System.out.println(result);
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        String result = param1 + param2 + " " + param3;
        System.out.println(result);
    }
}
