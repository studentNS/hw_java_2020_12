package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id){
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObjectFromResultSet(rs);
                }
                return null;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
    }

        private T createObjectFromResultSet(ResultSet resultSet) throws InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
            Object object = entityClassMetaData.getConstructor().newInstance();
            List<Field> objectFields = entityClassMetaData.getAllFields();
            for (Field objectField : objectFields) {
                Object value = resultSet.getObject(objectField.getName());
                objectField.setAccessible(true);
                objectField.set(object, value);
            }
            return (T) object;
        }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    clientList.add(createObjectFromResultSet(rs));
                }
                return clientList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(connection,
                    entitySQLMetaData.getInsertSql(),
                    getFieldValues(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldValues(T obj){
        List<Object> values = new ArrayList<>();
        try {
            for (Field field : entityClassMetaData.getFieldsWithoutId()){
                values.add(field.get(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(connection,
                    entitySQLMetaData.getUpdateSql(),
                    getFieldValues(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
