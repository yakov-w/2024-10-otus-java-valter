package ru.otus.jdbc.mapper;

import ru.otus.jdbc.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private String name;
    private Constructor<T> constructor;
    private Field fId;
    private final List<Field> allFields;
    private final List<Field> simpleFields;

    public EntityClassMetaDataImpl(Class<T> t) {
        this.name = t.getSimpleName();
        try {
            this.constructor = t.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        allFields = Arrays.asList(t.getDeclaredFields());
        Collections.sort(allFields, Comparator.comparing(Field::getName));
        List<Field> sF = new ArrayList<>(allFields.size()-1);
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                fId = field;
                continue;
            }
            sF.add(field);
        }
        simpleFields = sF;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return fId;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return simpleFields;
    }
}
