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

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Set<String> methodsWithAnnotations = new HashSet<>();

            for (Method methodSearchAnnotation : TestLogging.class.getMethods()) {
                if (methodSearchAnnotation.isAnnotationPresent(Log.class)) {
                    methodsWithAnnotations.add(methodSearchAnnotation.getName() + Arrays.toString(methodSearchAnnotation.getParameters()));
                }
            }

            if (methodsWithAnnotations.contains(method.getName() + Arrays.toString(method.getParameters()))) {
                System.out.println("executed method: " + method.getName() + ", param(s): " + Arrays.toString(args));
            }
            return method.invoke(testLogging, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + testLogging +
                    '}';
        }

    }
}
