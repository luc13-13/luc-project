package com.lc.framework.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 正则表达式工具类，封装匹配方法
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-03 13:52
 */
public class RegexUtils {

    public static String matchAndGetFirst(String regex, String targetString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(targetString);
        return matcher.find() ? targetString.substring(matcher.start(), matcher.end()) : null;
    }

}
