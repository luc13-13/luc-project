package com.lc.framework.data.permission.entity;

import com.lc.framework.data.permission.anno.DataColumn;
import com.lc.framework.data.permission.anno.DataScope;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 包装{@link DataScope}与{@link DataScopeEntity}, 方便通过ThreadLocal获取
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-03 11:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataScopeWrapper {
    private Map<Class<? extends IDataPermissionSqlHandler>, DataColumn> dataColumnMap;
    private List<IDataPermissionSqlHandler> includeHandlers;
    private boolean isMutex;
    private DataScopeEntity dataScopeEntity;
}
