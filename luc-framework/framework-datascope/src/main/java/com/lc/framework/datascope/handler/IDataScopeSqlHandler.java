package com.lc.framework.datascope.handler;

import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.datascope.entity.SupportTableDefinition;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

/**
 * <pre>
 *     权限条件构造接口，实现该接口可拓展权限表的处理方法。权限处理的方式是与权限模型高度耦合的，要紧密结合自身业务对权限模型的定义
 *     所有实现类的命名规范为XxxxDataScopeSqlHandler
 *
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 11:04
 */
public interface IDataScopeSqlHandler {
    String HANDLER_BEAN_SUFFIX = "DataScopeSqlHandler";

    /**
     * 将Table+DataScopeEntity转换为Expression
     *
     * @param table           sql语句中需要进行权限过滤的表
     * @param dataScopeEntity 当前用户的数据权限
     * @author Lu Cheng
     * @date 2023/11/17
     */
    Expression handleSelect(final Table table, DataScopeEntity dataScopeEntity);

    void bindTable(SupportTableDefinition tableDefinition);

    /**
     * 判断是否支持为当前数据对象构建过滤条件
     *
     * @param dataScopeEntity 用户持有的数据权限对象
     * @return true表示支持构建， false表示不支持构建
     * @author Lu Cheng
     * @date 2023/11/17
     */
    boolean supportDataScope(DataScopeEntity dataScopeEntity);


    /**
     * 判断是否支持为表增加条件，在初始化时通过DataScopeSqlHandlerCustomizer构建
     *
     * @param tableName 表的全限定名
     * @return true支持， false不支持
     * @author Lu Cheng
     * @date 2023/11/17
     */
    boolean supportTable(String tableName);

    /**
     * 获取权限处理器名称的默认方法, 例如SysUserDataScopeSqlHandler, 则返回SysUser
     */
    default String getName() {
        return getClass().getSimpleName().replace(HANDLER_BEAN_SUFFIX, "");
    }
}
