package ru.otus.jdbc.mapper;

import ru.otus.jdbc.core.repository.DataTemplate;
import ru.otus.jdbc.core.repository.DataTemplateException;
import ru.otus.jdbc.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S1068", "unchecked"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final Map<String, Field> fieldMap;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
        this.fieldMap = entityClassMetaData.getAllFields().stream().collect(Collectors.toMap(Field::getName, f -> f));
    }
    // TODO надо реализовать все методы

    // сюда надо вставить промежуточный слой, который сформирует sql запрос
    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                rs.next();
                return getInstance(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {
            var listResult = new ArrayList<T>();
            try {
                while (rs.next()) {
                    T instance = getInstance(rs);
                    listResult.add(instance);
                }

                return listResult;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).get();
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> objects = getListObjects(client);
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), objects);
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> objects = getListObjects(client);
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            objects.add(idField.get(client));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), objects);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> getListObjects(T client) {
        List<Object> objects = new ArrayList<>(entityClassMetaData.getFieldsWithoutId().size());
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                objects.add(field.get(client));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return objects;
    }

    private T getInstance(ResultSet rs) {
        try {
            T instance = entityClassMetaData.getConstructor().newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                String columnName = metaData.getColumnName(i);
                Field field = fieldMap.get(columnName);
                field.setAccessible(true);
                field.set(instance, rs.getObject(i));
            }
            return instance;
        } catch (ReflectiveOperationException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
