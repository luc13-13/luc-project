package com.lc.framework.data.permission.entity;

import com.lc.framework.data.permission.anno.DataPermission;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <pre>
 * 包装{@link DataPermission}与{@link DataScopeEntity}, 方便通过ThreadLocal获取
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
    private List<IDataPermissionSqlHandler> includeHandlers;
    private boolean isMutex;
    private DataScopeEntity dataScopeEntity;
}
