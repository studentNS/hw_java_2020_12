package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{

    private EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData){
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        String tableName = entityClassMetaData.getName();
        return "SELECT * FROM " + tableName.toLowerCase();
    }

    @Override
    public String getSelectByIdSql() {
        String tableName = entityClassMetaData.getName();
        String idField = entityClassMetaData.getIdField().getName();
        return "SELECT * FROM " + tableName.toLowerCase() + " WHERE " + idField + " = ?";
    }

    public String fieldWithoutIdStr(String strSql, List<Field> withoutId){
        for (Field field: withoutId) {
            strSql = strSql + field.getName() + ",";
        }
        return strSql;
    }

    @Override
    public String getInsertSql() {
        String tableName = entityClassMetaData.getName();
        List<Field> withoutId = entityClassMetaData.getFieldsWithoutId();
        String strSql = "INSERT INTO " + tableName.toLowerCase() + "(";

        strSql = fieldWithoutIdStr(strSql, withoutId);

        strSql = strSql.substring(0, strSql.length() - 1);
        strSql = strSql + ") values (";

        int countFiledWithoutId = entityClassMetaData.getFieldsWithoutId().size();
        for (int i = 1; i <= countFiledWithoutId; i++){
            strSql = strSql + "?,";
        }
        strSql = strSql.substring(0, strSql.length() - 1);
        strSql = strSql + ");";

        return strSql;
    }

    @Override
    public String getUpdateSql() {
        String tableName = entityClassMetaData.getName();
        String idField = entityClassMetaData.getIdField().getName();
        List<Field> withoutId = entityClassMetaData.getFieldsWithoutId();
        String strSql = "UPDATE " + tableName.toLowerCase() + " SET ";

        strSql = strSql + fieldWithoutIdStr(strSql, withoutId);

        strSql = strSql.substring(0, strSql.length() - 1);
        strSql = strSql + " WHERE " + idField + " = ?;";

        return strSql;
    }
}
