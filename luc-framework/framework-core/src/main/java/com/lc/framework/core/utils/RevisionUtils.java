package com.lc.framework.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 版本号生成工具类
 *
 * @author lucheng
 * @since 2026-02-09
 */
public final class RevisionUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private RevisionUtils() {
        // 私有构造器，防止实例化
    }

    /**
     * 生成带前缀的版本号
     * <p>
     * 格式：{prefix}{yyyyMMddHHmmss}
     * </p>
     *
     * @param prefix 版本号前缀，如 "V"、"SKU-"
     * @return 版本号字符串，如 "V20260209092251"
     */
    public static String generateTimestampRevision(String prefix) {
        String timestamp = generateTimestampRevision();
        return prefix != null ? prefix + timestamp : timestamp;
    }



    /**
     * 生成纯时间戳版本号（无前缀）
     * <p>
     * 格式：{yyyyMMddHHmmss}
     * </p>
     *
     * @return 时间戳字符串，如 "20260209092251"
     */
    public static String generateTimestampRevision() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
}
