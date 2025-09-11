package com.lc.framework.data.permission.entity;

import com.lc.framework.data.permission.handler.TenantDataPermissionSqlHandler;
import lombok.Data;

import java.util.List;

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
     * sql处理器名称，为IDataScopeSqlHandler的实现类，
     * 以{@link TenantDataPermissionSqlHandler} 为例， 配置文件中的handler = tenant， bean名称为tenantDataScopeSqlHandler
     */
    private String id;

    private List<SupportTableDefinition> supportTables;
}
