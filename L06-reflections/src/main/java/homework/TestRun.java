package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestRun<T> {

    private Map<String, Integer> resultMap = new HashMap<>();

    void runAllTests(Class<T> clazz) {
        test(clazz);
    }

    private void before(T testObj) {
        System.out.println("Before method:");
        for (Method method : testObj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                try {
                    ReflectionHelper.callMethod(testObj, method.getName());
                    resultMap.put("success", (resultMap.get("success") == null ? 0 : resultMap.get("success")) + 1);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    resultMap.put("problem", (resultMap.get("problem") == null ? 0 : resultMap.get("problem")) + 1);
                }
            }

        }
    }

    private void after(T testObj) {
        System.out.println("After method:");
        for (Method method : testObj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(After.class)) {
                try {
                    ReflectionHelper.callMethod(testObj, method.getName());
                    resultMap.put("success", (resultMap.get("success") == null ? 0 : resultMap.get("success")) + 1);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    resultMap.put("problem", (resultMap.get("problem") == null ? 0 : resultMap.get("problem")) + 1);
                }
            }
        }
    }

    private void test(Class<T> clazz) {
        System.out.println("Test method:");
        for (Method method : clazz.getDeclaredMethods()) {
            T testObj = ReflectionHelper.instantiate(clazz);
            if (method.isAnnotationPresent(Test.class)) {
                try {
                    this.before(testObj);
                    ReflectionHelper.callMethod(testObj, method.getName());
                    resultMap.put("success", (resultMap.get("success") == null ? 0 : resultMap.get("success")) + 1);
                    this.after(testObj);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    resultMap.put("problem", (resultMap.get("problem") == null ? 0 : resultMap.get("problem")) + 1);
                }
            }
        }

        System.out.println("Result:");
        int allTests = 0;
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            allTests = allTests + entry.getValue();
        }
        System.out.println("all " + allTests);

    }

}
