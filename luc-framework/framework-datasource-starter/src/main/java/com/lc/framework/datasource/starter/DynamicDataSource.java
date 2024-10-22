package com.lc.framework.datasource.starter;

import com.lc.framework.datasource.starter.provider.DynamicDataSourceProvider;
import com.lc.framework.datasource.starter.tool.DynamicDataSourceContextHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/8/12 11:10
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final List<DynamicDataSourceProvider> providers;

    @Getter
    @Setter
    private String primary = "master";

    public DynamicDataSource(List<DynamicDataSourceProvider> providers) {
        this.providers = providers;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.peek();
    }

    /**
     * 重写抽象类的方法，通过providers批量创建DataSource
     */
    @Override
    public void afterPropertiesSet() {
        if (providers == null) {
            throw new IllegalArgumentException("Property 'providers' is required!");
        }
        Map<Object, Object> providedDataSources = new HashMap<>(8);
        Map<String, DataSource> providedMap;
        for (DynamicDataSourceProvider provider : providers) {
            providedMap = provider.loadDataSources();
            if (providedMap != null && providedMap.size() > 0) {
                providedDataSources.putAll(providedMap);
            }
        }
        super.setTargetDataSources(providedDataSources);
        super.setDefaultTargetDataSource(providedDataSources.get(this.primary));

        super.afterPropertiesSet();
    }
}
