package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exception.ComponentNotFound;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
    }

    // Написать своего кода, чтобы приложение заработало.
    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }

        NavigableMap<Integer, List<Method>> methodss = getMethodss(configClass);

        try {
            Object instance = configClass.getConstructor().newInstance();
            for (List<Method> methods : methodss.values()) {
                for (Method method : methods) {
                    Class<?> returnType = method.getReturnType();
                    String name = method.getAnnotation(AppComponent.class).name();
                    if (appComponentsByName.containsKey(name)) {
                        throw new RuntimeException("В контексте не должно быть компонентов с одинаковым именем");
                    }
                    Parameter[] parameters = method.getParameters();
                    Object[] args = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> type = parameters[i].getType();
                        Object o = appComponents.stream()
                                .filter(type::isInstance)
                                .findFirst()
                                .get();
                        args[i] = o;
                    }
                    Object o = returnType.cast(method.invoke(instance, args));
                    appComponents.add(o);
                    appComponentsByName.put(name, o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private static NavigableMap<Integer, List<Method>> getMethodss(Class<?> configClass) {
        NavigableMap<Integer, List<Method>> methodss = new TreeMap<>();

        for (Method method : configClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                int order = method.getAnnotation(AppComponent.class).order();
                methodss.compute(order, (k, v) -> {
                    v = Objects.isNull(v) ? new ArrayList<>() : v;
                    v.add(method);
                    return v;
                });
            }
        }
        return methodss;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        try {
            if (appComponents.stream().filter(componentClass::isInstance).count() > 1) {
                throw new Exception();
            }
            return (C) appComponents.stream()
                    .filter(componentClass::isInstance)
                    .findFirst()
                    .orElseThrow(ComponentNotFound::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException(new ComponentNotFound());
        }
        return (C) appComponentsByName.get(componentName);
    }
}
