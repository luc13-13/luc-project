package com.lc.framework.data.permission.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.lc.framework.data.permission.anno.DataPermission;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * <pre>
 *     考虑到频繁调用反射方法带来的性能问题，可以对方法上的DataScope注解进行缓存(注解的定义在编译时已确定，不会被运行时修改)
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-01 17:15
 */
public class DataPermissionContextHolder {

    /**
     * 当前线程执行的方法名，与DATA_SCOPE_ANNO_CACHE中的key匹配
     */
    private static final ThreadLocal<DataPermission> DATA_PERMISSION_ANNO_LOCAL = new TransmittableThreadLocal<>();

    private static final ThreadLocal<Boolean> REWRITE = TransmittableThreadLocal.withInitial(() -> Boolean.FALSE);

    /**
     * 方法缓存，key为方法的全路径名， value为方法上的注解设置
     */
    private static final Map<String, DataPermission> DATA_PERMISSION_ANNO_CACHE = new ConcurrentHashMap<>();

    /**
     * 没有权限注解的方法
     */
    private static final Map<String, String> NOT_ANNOTATED_MAP_STATEMENT = new ConcurrentHashMap<>();

    public static void setDataPermissionLocal(DataPermission dataPermission) {
        DATA_PERMISSION_ANNO_LOCAL.set(dataPermission);
    }

    public static DataPermission getDataPermissionLocal() {
        return DATA_PERMISSION_ANNO_LOCAL.get();
    }

    public static void rewrite() {
        REWRITE.set(Boolean.TRUE);
    }

    /**
     * 从缓存中获取已解析的DataPermission， 如果缓存中没有，则由调用方负责提供DataScope的创建方法Supplier
     * 这里涉及到线程安全问题，先利用{@link ConcurrentHashMap#putIfAbsent}保证写的原子性，
     * 再从缓存中获取
     *
     * @param id       {@link DataPermissionContextHolder#DATA_PERMISSION_ANNO_CACHE}中的key
     * @param supplier 缓存缺失情况下的DataPermission获取方法， 由调用方提供
     * @author Lu Cheng
     * @create 2023/8/3
     */
    public static DataPermission getAnnotationCache(String id, Supplier<DataPermission> supplier) {
        if (!DATA_PERMISSION_ANNO_CACHE.containsKey(id)) {
            DataPermission dataPermission = supplier.get();
            if (dataPermission != null) {
                DATA_PERMISSION_ANNO_CACHE.putIfAbsent(id, dataPermission);
            } else {
                NOT_ANNOTATED_MAP_STATEMENT.put(id, id);
            }
            return dataPermission;
        }
        return DATA_PERMISSION_ANNO_CACHE.get(id);
    }

    public static void addNotAnnotatedMapStatement(MappedStatement ms) {
        NOT_ANNOTATED_MAP_STATEMENT.putIfAbsent(ms.getId(), ms.getId());
    }/**/

    public static boolean willIgnored(String id) {
        return NOT_ANNOTATED_MAP_STATEMENT.containsKey(id);
    }

    /**
     * 一定要规范使用ThreadLocal， 用完之后要调用remove删除，否则容易发生OOM
     *
     * @author Lu Cheng
     * @create 2023/8/5
     */
    public static void clear() {
        DATA_PERMISSION_ANNO_LOCAL.remove();
        REWRITE.remove();
    }


}
