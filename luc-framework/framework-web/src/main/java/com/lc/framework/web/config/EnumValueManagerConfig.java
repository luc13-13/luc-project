package com.lc.framework.web.config;

import com.lc.framework.core.utils.validator.EnumValueManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 10/2/26 09:29
 * @version : 1.0
 */
@Configuration
public class EnumValueManagerConfig {

    @ConditionalOnMissingBean
    public EnumValueManager defaultEnumValueManager() {
        return new EnumValueManager() {
            @Override
            public Set<String> getEnumValues(String enumName) {
                return EnumValueManager.super.getEnumValues(enumName);
            }
        };
    }
}
