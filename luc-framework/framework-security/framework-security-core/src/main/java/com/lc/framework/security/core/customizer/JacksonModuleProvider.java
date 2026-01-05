package com.lc.framework.security.core.customizer;

import tools.jackson.databind.JacksonModule;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 29/12/25 15:53
 * @version : 1.0
 */
@FunctionalInterface
public interface JacksonModuleProvider {
    List<JacksonModule> getModules();
}
