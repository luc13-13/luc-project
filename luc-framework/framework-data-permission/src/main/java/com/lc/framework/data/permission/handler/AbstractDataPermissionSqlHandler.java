package com.lc.framework.data.permission.handler;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lc.framework.core.constants.StringConstants;
import com.lc.framework.data.permission.entity.DataPermissionProperties;
import com.lc.framework.data.permission.entity.SupportTableDefinition;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/12 14:22
 * @version : 1.0
 */
public abstract class AbstractDataPermissionSqlHandler implements IDataPermissionSqlHandler{

    /**
     * 封装权限表及权限字段<br/>
     * key: database_name.table_name, <br/>value: [column_name1, column_name2]
     */
    protected Map<String, Set<String>> supportedTableMap = new ConcurrentHashMap<>();

    @Override
    public void bindTable(SupportTableDefinition tableDefinition) {
        // 兼容不指定数据库名称的情况
        String fullyQualifiedName = StringUtils.isNotEmpty(tableDefinition.getDatabase()) ? (tableDefinition.getDatabase() + StringConstants.DOT + tableDefinition.getTableName()) : tableDefinition.getTableName();
        if (supportedTableMap.containsKey(fullyQualifiedName)) {
            supportedTableMap.get(fullyQualifiedName).add(tableDefinition.getColumnName());
        } else  {
            Set<String> columnSet = new ConcurrentSkipListSet<>();
            columnSet.add(tableDefinition.getColumnName());
            supportedTableMap.put(fullyQualifiedName, columnSet);
        }
    }

    /**
     * 兼容sql未指定数据库名称的情况，需要设置{@link DataPermissionProperties#isIgnoreDatabaseName()}为true
     */
    protected String getTableName(Table table) {
        return StringUtils.isNotEmpty(table.getUnquotedDatabaseName()) ? (table.getUnquotedDatabaseName() + StringConstants.DOT + table.getUnquotedName()) : table.getUnquotedName();
    }

    /**
     * 使用别名方式声明列
     *
     * @param table 表对象
     * @param columnName 列名
     * @return 字段
     */
    protected Column getAliasColumn(Table table, String columnName) {
        StringBuilder column = new StringBuilder();
        // todo 该起别名就要起别名,禁止修改此处逻辑
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(StringPool.DOT);
        }
        column.append(columnName);
        return new Column(column.toString());
    }

    protected Set<String> getColumns(String tableName) {
        return  supportedTableMap.get(tableName);
    }

    @Override
    public boolean supportTable(final Table table) {
        String tableName = getTableName(table);
        return supportedTableMap.containsKey(tableName) && !CollectionUtils.isEmpty(supportedTableMap.get(tableName));
    }
}
