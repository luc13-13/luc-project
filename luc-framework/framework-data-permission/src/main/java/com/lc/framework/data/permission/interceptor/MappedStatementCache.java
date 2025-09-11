package com.lc.framework.data.permission.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lc.framework.data.permission.anno.DataScope;
import com.lc.framework.data.permission.handler.IDataPermissionSqlHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-21 11:15
 */
public class MappedStatementCache {
    /**
     * 方法缓存，key为方法的全路径名， value为方法上的注解设置
     */
    private static final Map<String, DataScope> DATA_SCOPE_ANNO_CACHE = new ConcurrentHashMap<>();

    /**
     * 没有权限注解的方法
     */
    private static final Map<String, String> NOT_ANNOTATED_MAP_STATEMENT = new ConcurrentHashMap<>();

    /**
     * 有权限注解，但是解析后未对SQL进行修改的方法
     */
    private static final Map<Class<? extends IDataPermissionSqlHandler>, Set<String>> NO_REWRITE_MAP_STATEMENTS = new ConcurrentHashMap<>();

    /**
     * 从缓存中获取已解析的DataScope， 如果缓存中没有，则由调用方负责提供DataScope的创建方法Supplier
     * 这里涉及到线程安全问题，先利用{@link ConcurrentHashMap#putIfAbsent}保证写的原子性，
     * 再从缓存中获取
     *
     * @param id       {@link MappedStatementCache#DATA_SCOPE_ANNO_CACHE}中的key
     * @param supplier 缓存缺失情况下的DataScope生产方法， 由调用方提供
     * @author Lu Cheng
     * @create 2023/8/3
     */
    public static DataScope getDataScopeAnno(String id, Supplier<DataScope> supplier) {
        if (!DATA_SCOPE_ANNO_CACHE.containsKey(id)) {
            DataScope dataScope = supplier.get();
            if (dataScope != null) {
                DATA_SCOPE_ANNO_CACHE.putIfAbsent(id, dataScope);
            } else {
                NOT_ANNOTATED_MAP_STATEMENT.put(id, id);
            }
            return dataScope;
        }
        return DATA_SCOPE_ANNO_CACHE.get(id);
    }

    public static boolean isNotAnnotated(MappedStatement ms) {
        return NOT_ANNOTATED_MAP_STATEMENT.containsKey(ms.getId());
    }

    public static void addNotAnnotatedMapStatement(MappedStatement ms) {
        NOT_ANNOTATED_MAP_STATEMENT.putIfAbsent(ms.getId(), ms.getId());
    }

    public static void addIgnoredMapStatement(MappedStatement ms, List<IDataPermissionSqlHandler> handlers) {
        if (DataScopeContextHolder.isRewrite()) {
            return;
        }
        for (IDataPermissionSqlHandler handler : handlers) {
            if (NO_REWRITE_MAP_STATEMENTS.containsKey(handler.getClass())) {
                NO_REWRITE_MAP_STATEMENTS.get(handler.getClass()).add(ms.getId());
            } else {
                Set<String> set = new HashSet<>();
                set.add(ms.getId());
                NO_REWRITE_MAP_STATEMENTS.put(handler.getClass(), set);
            }
        }
    }

    public static boolean willIgnored(MappedStatement ms, List<IDataPermissionSqlHandler> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            return true;
        }
        for (IDataPermissionSqlHandler handler : handlers) {
            if (NO_REWRITE_MAP_STATEMENTS.containsKey(handler.getClass()) && NO_REWRITE_MAP_STATEMENTS.get(handler.getClass()).contains(ms.getId())) {
                return false;
            }
        }
        return true;
    }
}
