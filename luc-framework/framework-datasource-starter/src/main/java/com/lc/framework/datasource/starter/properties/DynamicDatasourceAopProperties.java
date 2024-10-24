package com.lc.framework.datasource.starter.properties;

import lombok.Data;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/23 17:27
 */
@Data
public class DynamicDatasourceAopProperties {
    /**
     * 开启AOP
     */
    private boolean enabled = true;

    /**
     * 是否开启基于表达式的AOP方法，true开始，false不开启<br/>
     * 要求为每个DataSource配置pointcut表达式
     */
    private boolean expressionEnabled = false;
}
