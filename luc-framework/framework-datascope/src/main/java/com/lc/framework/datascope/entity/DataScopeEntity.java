package com.lc.framework.datascope.entity;

import com.lc.framework.datascope.anno.DataColumn;
import com.lc.framework.datascope.anno.DataScope;
import com.lc.framework.datascope.interceptor.DataScopeInterceptor;

import java.util.List;

/**
 * 权限属性
 * 目前支持两种权限校验方式：基于注解{@link DataScope}, {@link DataColumn}和基于mybatis-plus拦截插件的
 * TODO: 将DataScopeEntity优化为接口， 规范接口方法给
 *
 * @author Lu Cheng
 * @create 2023-07-31 17:01
 * @see DataScopeInterceptor
 */
public interface DataScopeEntity {

    // 必备属性， 后续优化为方法 String getUserId()
//    private String userId;

    String getUserId();

    String getCurrentRole();

    String getCurrentDeptId();

    String getCurrentRegionId();

    List<String> getRoleIds();

    List<String> getPermissionIds();

    List<String> getDeptIds();

    List<String> getRegionIds();
}
