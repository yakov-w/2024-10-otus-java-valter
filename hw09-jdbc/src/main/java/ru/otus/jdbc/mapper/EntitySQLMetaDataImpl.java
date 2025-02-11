package ru.otus.jdbc.mapper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String selectAllSQL;
    private final String selectByIdSQL;
    private final String insertSQL;
    private final String updateSQL;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        selectAllSQL = String.format("SELECT * FROM %s", entityClassMetaData.getName()).toLowerCase();
        selectByIdSQL = String.format("SELECT * FROM %s WHERE %s = ?", entityClassMetaData.getName().toLowerCase(), entityClassMetaData.getIdField().getName().toLowerCase());
        insertSQL = generateInsert(entityClassMetaData);
        updateSQL = generateUpdate(entityClassMetaData);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSQL;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSQL;
    }

    @Override
    public String getInsertSql() {
        return insertSQL;
    }

    @Override
    public String getUpdateSql() {
        return updateSQL;
    }

    private String generateInsert(EntityClassMetaData entityClassMetaData) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(entityClassMetaData.getName().toLowerCase());
        sb.append(" (");

        for (int i = 0; i < entityClassMetaData.getFieldsWithoutId().size(); i++) {
            if (i>0 && i< entityClassMetaData.getFieldsWithoutId().size()) {
                sb.append(", ");
            }
            Field o = (Field) entityClassMetaData.getFieldsWithoutId().get(i);
            sb.append(o.getName());
        }
        sb.append(") VALUES (");
        for (int i = 0; i < entityClassMetaData.getFieldsWithoutId().size(); i++) {
            if (i>0 && i< entityClassMetaData.getFieldsWithoutId().size()) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }

    private String generateUpdate(EntityClassMetaData entityClassMetaData) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(entityClassMetaData.getName().toLowerCase());
        sb.append(" SET ");

        for (int i = 0; i < entityClassMetaData.getFieldsWithoutId().size(); i++) {
            if (i>0 && i< entityClassMetaData.getFieldsWithoutId().size()) {
                sb.append(", ");
            }
            Field o = (Field) entityClassMetaData.getFieldsWithoutId().get(i);
            sb.append(o.getName());
            sb.append(" = ?");
        }
        sb.append(" WHERE ");
        sb.append(entityClassMetaData.getIdField().getName());
        sb.append(" = ?");
        return sb.toString();
    }
}
