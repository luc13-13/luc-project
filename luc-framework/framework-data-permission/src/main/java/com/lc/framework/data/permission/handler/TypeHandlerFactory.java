package com.lc.framework.data.permission.handler;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.type.JdbcType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/8/5 17:04
 */
public class TypeHandlerFactory {
    private static Map<JdbcType, Function<Object, Expression>> EXPRESSION_JDBCTYPE_HANDLER_MAP = new ConcurrentHashMap<>();

    static{
        EXPRESSION_JDBCTYPE_HANDLER_MAP.put(JdbcType.BIGINT, params -> new LongValue((Long) params));
        EXPRESSION_JDBCTYPE_HANDLER_MAP.put(JdbcType.VARCHAR, params -> new StringValue((String) params));
    }

    public Expression transformJdbcType2Expression(Object params, JdbcType jdbcType) {
        return TypeHandlerFactory.EXPRESSION_JDBCTYPE_HANDLER_MAP.get(jdbcType).apply(params);
    }

    public void registerHandler(JdbcType jdbcType, Function<Object, Expression> function) {
        TypeHandlerFactory.EXPRESSION_JDBCTYPE_HANDLER_MAP.put(jdbcType, function);
    }

}
