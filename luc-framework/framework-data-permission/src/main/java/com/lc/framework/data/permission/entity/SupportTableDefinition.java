package com.lc.framework.data.permission.entity;

import lombok.Data;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-21 10:22
 */
@Data
public class SupportTableDefinition {

    private String database;
    /**
     * 需要进行数据过滤的表名
     */
    private String tableName;

    /**
     * 数据过滤依赖的列名
     */
    private String columnName;

    /**
     * 列数据类型, 默认为VARCHAR
     */
    private String columnType = "VARCHAR";
}
