package ru.otus.jdbc.mapper;

import ru.otus.jdbc.annotations.Id;
import ru.otus.jdbc.core.repository.DataTemplate;
import ru.otus.jdbc.core.repository.DataTemplateException;
import ru.otus.jdbc.core.repository.executor.DbExecutor;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.model.Manager;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S1068", "unchecked"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }
    // TODO надо реализовать все методы

    // сюда надовставить проежуточный слой который сформирует sql запрос
    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                Constructor<T> c = createContents(metaData);
                Object[] objects = new Object[metaData.getColumnCount()];
                rs.next();
                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    rs.getObject(i);
                }
                return c.newInstance(objects);
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {
            var listResult = new ArrayList<T>();
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                Constructor<T> c = createContents(metaData);
                while (rs.next()) {
                    Object[] objects = new Object[metaData.getColumnCount()];
                    for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                        rs.getObject(i);
                    }
                    listResult.add(c.newInstance(objects));
                }
                return listResult;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }).get();
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Method> methods = Arrays.asList(client.getClass().getDeclaredMethods());
            Collections.sort(methods, Comparator.comparing(Method::getName));
            Field fId = Arrays.stream(client.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
            List<Object> objects = new ArrayList<>(methods.size());
            for (Method method : methods) {
                T invoke;
                if (method.getName().contains("get") && !method.getName().toLowerCase().contains(fId.getName().toLowerCase())) {
                    invoke = (T) method.invoke(client);
                    objects.add(invoke);
                }
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), objects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Method> methods = Arrays.asList(client.getClass().getDeclaredMethods());
            Collections.sort(methods, Comparator.comparing(Method::getName));
            Field fId = Arrays.stream(client.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
            List<Object> objects = new ArrayList<>(methods.size());
            Object idVal = null;
            for (Method method : methods) {
                T invoke;
                if (method.getName().contains("get")) {
                    if (method.getName().toLowerCase().contains(fId.getName().toLowerCase())) {
                        idVal = (T) method.invoke(client);
                    } else {
                        invoke = (T) method.invoke(client);
                        objects.add(invoke);
                    }
                }
            }
            if (idVal == null) {
                throw new RuntimeException("Id field nit found");
            }
            objects.add(idVal);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), objects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Constructor<T> createContents(ResultSetMetaData metaData) {
        try {
            String t = metaData.getTableName(1);
            String nameClass = t.substring(0, 1).toUpperCase() + t.substring(1);
            Class<?> clazz = Class.forName("ru.otus.jdbc.crm.model." + nameClass);
            for (Constructor<?> c : clazz.getConstructors()) {
                if (c.getParameterTypes().length == metaData.getColumnCount()) {
                    int i = 0;
                    int j = 0;
                    Class<?>[] types = c.getParameterTypes();
                    for (; i < metaData.getColumnCount(); i++) {
                        String columnTypeName = metaData.getColumnClassName(i + 1);
                        String s = types[i].getName();
                        if (columnTypeName.equals(types[i].getName())) {
                            j++;
                        }
                    }
                    if (i == j) {
                        return (Constructor<T>) c;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
