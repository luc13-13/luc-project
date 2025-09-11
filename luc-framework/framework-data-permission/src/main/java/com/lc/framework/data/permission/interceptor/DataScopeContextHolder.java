package com.lc.framework.data.permission.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.lc.framework.data.permission.entity.DataScopeEntity;
import com.lc.framework.data.permission.entity.DataScopeWrapper;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;

import java.util.List;

/**
 * <pre>
 *     考虑到频繁调用反射方法带来的性能问题，可以对方法上的DataScope注解进行缓存(注解的定义在编译时已确定，不会被运行时修改)
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-01 17:15
 */
public class DataScopeContextHolder {

    /**
     * 当前线程执行的方法名，与DATA_SCOPE_ANNO_CACHE中的key匹配
     */
    private static final ThreadLocal<DataScopeWrapper> DATA_SCOPE_WRAPPER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    private static final ThreadLocal<Boolean> REWRITE = TransmittableThreadLocal.withInitial(() -> Boolean.FALSE);

    public static void putDataScopeWrapper(boolean isMutex, DataScopeEntity dataScopeEntity, List<IDataPermissionSqlHandler> handlers) {
        DATA_SCOPE_WRAPPER_THREAD_LOCAL.set(DataScopeWrapper.builder()
                .isMutex(isMutex)
                .includeHandlers(handlers)
                .dataScopeEntity(dataScopeEntity).build());
    }

    public static DataScopeWrapper getDataScopeWrapper() {
        return DATA_SCOPE_WRAPPER_THREAD_LOCAL.get();
    }

    public static void rewrite() {
        REWRITE.set(Boolean.TRUE);
    }

    public static boolean isRewrite() {
        return REWRITE.get();
    }

    /**
     * 一定要规范使用ThreadLocal， 用完之后要调用remove删除，否则容易发生OOM
     *
     * @author Lu Cheng
     * @create 2023/8/5
     */
    public static void clear() {
        DATA_SCOPE_WRAPPER_THREAD_LOCAL.remove();
        REWRITE.remove();
    }


}
