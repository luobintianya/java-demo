package com.ex.framework.database.impl;

import com.ex.framework.database.ColumnMeta;
import com.ex.framework.database.DataMeta;
import com.ex.framework.database.SqlMetaBuilder;
import com.ex.framework.database.annotation.Table;
import com.ex.framework.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
* @Author: lixubin
* @Date: 2019-07-22
* @Description: 基于mysql的基本增删改查操作实现类
*/
@Service
public class MySqlMetaBuilderImpl implements SqlMetaBuilder {
    @Override
    public <T> DataMeta insert(T model) {
        ColumnMeta columnMeta = ReflectionUtil.getColumnMeta(model);
        Object[] columnName = columnMeta.getNames();
        Object[] columnValue = columnMeta.getValues();

        StringBuffer questionMarkStr = new StringBuffer();
        for (int i = 0; i < columnValue.length; i++) {
            questionMarkStr.append("?");
            if ((i+1)!=columnValue.length){
                questionMarkStr.append(",");
            }
        }
        Table table = model.getClass().getAnnotation(Table.class);
        String columnNameStr = StringUtils.join(columnName, ",");

        String addSql = "INSERT INTO "+table.name()+" ("+columnNameStr+") VALUES ("+questionMarkStr.toString()+")";

        DataMeta dataMeta = new DataMeta();
        dataMeta.setSql(addSql);
        dataMeta.setParamter(columnValue);
        return dataMeta;
    }

    @Override
    public <T> DataMeta update(T model, Integer id) {
        ColumnMeta columnMeta = ReflectionUtil.getColumnMeta(model);
        Object[] columnName = columnMeta.getNames();
        Object[] columnValue = columnMeta.getValues();

        String columnId = ReflectionUtil.getPrimaryKey(model.getClass());
        Table table = model.getClass().getAnnotation(Table.class);

        List valueList = new ArrayList();
        StringBuffer setStr = new StringBuffer();
        for (int i = 0; i < columnName.length; i++) {
            setStr.append(columnName[i]+"=?");
            valueList.add(columnValue[i]);
            if ((i+1)!=columnName.length){
                setStr.append(",");
            }
        }
        String editSql ="UPDATE "+table.name()+" SET "+setStr.toString()+" WHERE "+columnId+"=?";
        valueList.add(id);

        DataMeta dataMeta = new DataMeta();
        dataMeta.setSql(editSql);
        dataMeta.setParamter(valueList.toArray());
        return dataMeta;
    }

    @Override
    public String queryForModel(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        String columnId = ReflectionUtil.getPrimaryKey(clazz);
        String querySql = "SELECT * FROM "+table.name()+" WHERE "+columnId+"=?";
        return querySql;
    }

    @Override
    public String delete(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        String columnId = ReflectionUtil.getPrimaryKey(clazz);
        String deleteSql = "DELETE FROM "+table.name()+" WHERE "+columnId+"=?";
        return deleteSql;
    }
}
