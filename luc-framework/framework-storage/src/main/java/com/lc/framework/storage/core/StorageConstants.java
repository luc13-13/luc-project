package com.lc.framework.storage.core;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/21 10:49
 */
public class StorageConstants {

    public static enum StorageTypeEnum {
        /**
         * 存储类型枚举类
         */
        OSS_QINIU("qiniu"),
        OSS_LOCAL("local");

        public final String type;

        StorageTypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }
}
