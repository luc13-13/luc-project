package com.lc.framework.data.permission.entity;

import com.lc.framework.data.permission.handler.AbstractDataPermissionSqlHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.List;

/**
 * 权限拦截器配置类， 要求涉及到权限的数据库中对数据库和表名的引用没有引号，因此要求数据库和表命名时不与关键字冲突
 * 例如yml配置文件中进行了如下配置：
 * <pre>
 *  {@code
 *  data-scope:
 *    tables:
 *      - table-name: auth_center
 *        column_name: id
 *  }
 * {@code
 * select u.name from auth_center.sys_user u
 * }
 * </pre>
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/8/5 11:16
 */
@Data
@Scope(value = "refresh", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ConfigurationProperties(prefix = "data-permission")
public class DataPermissionProperties {

    /**
     * 记录权限的表信息
     */
    private List<HandlerDefinition> handlerDefinition;

    /**
     * 是否开启权限拦截器：false不开启， true开启
     */
    private boolean enabled = false;

    /**
     * 是否忽略数据库名，false不忽略，true忽略。决定了{@link AbstractDataPermissionSqlHandler}中权限表的处理方式
     */
    private boolean ignoreDatabaseName = true;

}
