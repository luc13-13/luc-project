package com.lc.framework.data.permission.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 封装权限表和权限字段,
 *
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/8/5 11:57
 */
@Data
public class HandlerDefinition {
    /**
     * sql处理器名称，为IDataPermissionSqlHandler的实现类，
     * 以TenantDataPermissionSqlHandler为例， 配置文件中的handler = tenant， bean名称为tenantDataPermissionSqlHandler
     */
    private String id;

    /**
     * 处理器支持的权限表格定义<br/>
     * key: database_name <br/> value: 表格及列定义
     */
    private Map<String, List<SupportTableDefinition>> supportTables;
}
