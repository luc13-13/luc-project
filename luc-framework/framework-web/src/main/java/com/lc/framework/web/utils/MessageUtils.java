package com.lc.framework.web.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/27 16:52
 * @version : 1.0
 */
public class MessageUtils {
    public static String getMessage(String messageCode) {
        MessageSource messageSource = SpringBeanUtil.getBean("messageSource");
        return messageSource.getMessage(messageCode, null, LocaleContextHolder.getLocale());
    }
}
