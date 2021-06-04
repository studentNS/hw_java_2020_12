package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData{

    private Class<T> clazz;
    private Constructor<T> constructor;


    public EntityClassMetaDataImpl(Class<T> clazz) throws NoSuchMethodException {
        this.clazz = clazz;
        this.constructor = constructor();
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public <T> Constructor<T> getConstructor() {

        return (Constructor<T>) constructor;
    }

    private Constructor<T> constructor() throws NoSuchMethodException {
        return clazz.getConstructor();
    }

    @Override
    public Field getIdField() {
        Field fieldResult = null;
        for (Field field : getAllFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldResult = field;
                field.setAccessible(true);
                return fieldResult;
            }
        }
        return fieldResult;
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> fieldList = new ArrayList<>();

        for (Field field : getAllFields()) {

            if (!field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
