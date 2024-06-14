package com.lc.auth.gateway.factory;

import com.lc.auth.gateway.enums.RequiredHeaderEnum;
import com.lc.auth.gateway.strategy.HeaderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-04 10:10
 */
@Slf4j
@Component
public class HeaderGeneratorFactory {

    /**
     * 所有HeaderGenerator的beanName后缀
     */
    public static final String BEAN_SUFFIX = "Generator";

    private static final Map<RequiredHeaderEnum, HeaderGenerator> headerSupplierStrategy = new ConcurrentHashMap<>();

    public static void register(RequiredHeaderEnum type, HeaderGenerator bean) {
        headerSupplierStrategy.put(type, bean);
    }

    public static HeaderGenerator getByType(RequiredHeaderEnum type) {
        return headerSupplierStrategy.get(type);
    }

    public static void test() {
        log.info("请求头填充策略缓存: {}",headerSupplierStrategy.size());
        headerSupplierStrategy.forEach((k ,v) -> log.info("缓存:{}", k));
    }
}
