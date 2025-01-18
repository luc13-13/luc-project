package com.lc.framework.storage.core;

import lombok.Getter;

/**
 * <pre>
 *     存储相关的枚举类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/21 10:49
 */
public class StorageConstants {

    /**
     * 存储类型枚举类
     */
    @Getter
    public enum StorageTypeEnum {

        OSS_STORAGE("oss"),
        OSS_LOCAL("local");

        public final String type;

        StorageTypeEnum(String type) {
            this.type = type;
        }

    }

    /**
     * StorageClientTemplate加载顺序。只允许存在一个实例
     */
    @Getter
    public enum StorageClientOrder {

        OSS_S3(1),
        OSS_ASYNC(2),
        LOCAL(3);

        public final Integer order;

        StorageClientOrder(Integer order) { this.order = order; }
    }
}
