package com.lc.framework.datascope.entity;

import com.lc.framework.datascope.anno.DataColumn;
import com.lc.framework.datascope.anno.DataScope;
import com.lc.framework.datascope.interceptor.DataScopeInterceptor;

import java.util.List;

/**
 * 权限属性
 * 目前支持两种权限校验方式：基于注解{@link DataScope}, {@link DataColumn}和基于mybatis-plus拦截插件的
 *
 * @author Lu Cheng
 * @create 2023-07-31 17:01
 * @see DataScopeInterceptor
 */
public interface DataScopeEntity {

    /**
     * 获取userId
     * @return userId
     */
    String getUserId();

    /**
     * 获取当前角色
     * @return roleId
     */
    String getCurrentRole();

    /**
     * 获取当前部门
     * @return deptId
     */
    String getCurrentDeptId();

    /**
     * 获取当前区域
     * @return regionId
     */
    String getCurrentRegionId();

    /**
     * 获取角色列表
     * @return roleIdList
     */
    List<String> getRoleIds();

    /**
     * 获取权限列表
     * @return permissionIdList
     */
    List<String> getPermissionIds();

    /**
     * 获取部门列表
     * @return deptIdList
     */
    List<String> getDeptIds();

    List<String> getRegionIds();
}
