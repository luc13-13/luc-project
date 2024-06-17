package com.lc.system.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.lc.framework.datascope.config.DataScopeSqlHandlerCustomizer;
import com.lc.framework.datascope.entity.SupportTableDefinition;
import com.lc.framework.datascope.handler.TenantDataScopeSqlHandler;
import com.lc.framework.datascope.interceptor.DataScopeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-11-16 11:29
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor interceptor(DataScopeInterceptor dataScopeInterceptor) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(dataScopeInterceptor);
        return interceptor;
    }

    @Bean
    public DataScopeSqlHandlerCustomizer<TenantDataScopeSqlHandler> customizer() {
        SupportTableDefinition p = new SupportTableDefinition();
        p.setDatabase("institution_center");
        p.setColumnName("test_column");
        p.setTableName("test_table");
        return handler -> handler.bindTable(p);
    }

}
