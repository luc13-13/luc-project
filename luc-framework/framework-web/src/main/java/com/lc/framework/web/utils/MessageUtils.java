package com.lc.framework.web.utils;

import jakarta.annotation.Nonnull;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  消息工具类 </br>
 *  支持国际化：</br>
 *  （1）增加国际化文件目录
 *     <pre>
 *         resources/i18n/messages.properties
 *         resources/i18n/messages_zh_CN.properties
 *         resources/i18n/messages_en_US.properties
 *     </pre>
 *  （2）配置文件增加消息配置
 *  <pre>
 *     {@code
 *     spring:
 *       messages:
 *         basename: i18n/messages
 *         encoding: UTF-8
 *     }
 * </pre>
 * <p/>
 * @author : Lu Cheng
 * @date : 2025/8/27 16:52
 * @version : 1.0
 */
@Component
public class MessageUtils implements MessageSourceAware {

    public static MessageSource messageSource;

    public static String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, LocaleContextHolder.getLocale());
    }

    @Override
    public void setMessageSource(@Nonnull MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }
}
