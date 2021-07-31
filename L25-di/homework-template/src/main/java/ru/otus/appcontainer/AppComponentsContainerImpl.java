package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);
        // You code here...
        Object obj = getInstance(configClass);

        Method[] methods = configClass.getDeclaredMethods();

        Map<Integer, List<Method>> sortMethodOrderMap = sortMethodOrder(methods);

        for (Integer i : sortMethodOrderMap.keySet()) {
            List<Method> currentMethods = sortMethodOrderMap.get(i);
            for (Method method : currentMethods) {
                getParameterMethod(method, obj);
            }
        }

    }

    private Map<Integer, List<Method>> sortMethodOrder(Method[] methods) {

        Map<Integer, List<Method>> sortMethodOrderMap = new HashMap<>();

        for (Method method : methods) {
            var annotationOrder = method.getAnnotation(AppComponent.class).order();
            if (!sortMethodOrderMap.containsKey(annotationOrder)) {
                List<Method> newMethodsList = new ArrayList<>();
                newMethodsList.add(method);
                sortMethodOrderMap.put(annotationOrder, newMethodsList);
            } else {
                List<Method> currentMethodsList = sortMethodOrderMap.get(annotationOrder);
                currentMethodsList.add(method);
                sortMethodOrderMap.replace(annotationOrder, currentMethodsList);
            }
        }
        return sortMethodOrderMap;
    }

    private Object getInstance(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.isInstance(component)) {
                return (C) component;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private void getParameterMethod(Method method, Object obj) throws InvocationTargetException, IllegalAccessException {

        var annotationName = method.getAnnotation(AppComponent.class).name();
        var methodParameters = method.getParameterTypes();
        Object[] objects = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            var currentType = methodParameters[i];
            objects[i] = getAppComponent(currentType);
        }

        Object objCurr = method.invoke(obj, objects);
        appComponentsByName.put(annotationName, objCurr);
        appComponents.add(objCurr);
    }

}
