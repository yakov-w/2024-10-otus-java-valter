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
        checkConfigClass(initialConfigClass);
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... claszz) {
        Arrays.stream(claszz).forEach(this::checkConfigClass);

        Arrays.stream(claszz).sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class)
                .order()));
        Arrays.stream(claszz).forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        Method[] methods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        m -> m.getAnnotation(AppComponent.class).order()))
                .toArray(Method[]::new);

        try {
            Object instance = configClass.getConstructor().newInstance();
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
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
