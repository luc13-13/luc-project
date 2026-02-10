package com.lc.framework.core.utils.validator;

import java.util.Set;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 10/2/26 08:54
 * @version : 1.0
 */
public interface EnumValueManager {

    default Set<String> getEnumValues(String enumName) {
        return Set.of();
    }

}
