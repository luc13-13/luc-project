package com.lc.framework.data.permission.handler;

import com.lc.framework.data.permission.entity.SupportTableDefinition;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.List;

/**
 * <pre>
 *     权限条件构造接口，实现该接口可拓展权限表的处理方法。权限处理的方式是与权限模型高度耦合的，要紧密结合自身业务对权限模型的定义
 *     所有实现类的命名规范为XxxxDataPermissionSqlHandler
 *
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 11:04
 */
public interface IDataPermissionSqlHandler {
    String HANDLER_BEAN_SUFFIX = "DataPermissionSqlHandler";

    /**
     * 将Table转换为Expression
     *
     * @param table           sql语句中需要进行权限过滤的表
     * @author Lu Cheng
     * @date 2023/11/17
     */
    Expression getExpression(final Table table);

    void bindTable(SupportTableDefinition tableDefinition);


    /**
     * 判断是否支持为表增加条件，在初始化时通过DataScopeSqlHandlerCustomizer构建
     *
     * @param table 表
     * @return true支持， false不支持
     * @author Lu Cheng
     * @date 2023/11/17
     */
    boolean supportTable(final Table table);

    default boolean ignoreInsert(List<Column> columns) {
        return true;
    }

    /**
     * 获取权限处理器名称的默认方法, 例如SysUserDataPermissionSqlHandler, 则返回SysUser
     */
    default String getName() {
        return getClass().getSimpleName().replace(HANDLER_BEAN_SUFFIX, "");
    }
}
