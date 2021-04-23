package homework;

import homework.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Ioc {

    private Ioc() {
    }

    static TestLogging createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;
        private Set<String> methodsWithAnnotations = new HashSet<>();

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
            selectMethodsWithAnnotations();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (methodsWithAnnotations.contains(method.getName() + Arrays.toString(method.getParameters()))) {
                System.out.println("executed method: " + method.getName() + ", param(s): " + Arrays.toString(args));
            }
            return method.invoke(testLogging, args);
        }

        public void selectMethodsWithAnnotations(){

            for (Method method : TestLoggingImpl.class.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    methodsWithAnnotations.add(method.getName() + Arrays.toString(method.getParameters()));
                }
            }
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + testLogging +
                    '}';
        }

    }
}
