package com.lc.framework.storage.core;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.local.LocalStorageClientTemplate;
import com.lc.framework.storage.oss.sync.OssClientTemplate;
import com.lc.framework.storage.oss.async.OssAsyncClientTemplate;
import lombok.Getter;
import org.springframework.core.Ordered;

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

        OSS_S3(OssClientTemplate.class, 1),
        OSS_ASYNC(OssAsyncClientTemplate.class, 2),
        LOCAL(LocalStorageClientTemplate.class, 3);

        public final Class<? extends StorageClientTemplate> clazz;
        public final Integer order;

        StorageClientOrder(Class<? extends StorageClientTemplate> clazz, Integer order) {
            this.clazz = clazz;
            this.order = order;
        }

        public static Integer getOrder(Class<? extends StorageClientTemplate> clazz ) {
            for (StorageClientOrder item : StorageClientOrder.values()) {
                if (item.clazz.equals(clazz)) {
                    return item.getOrder();
                }
            }
            return Ordered.LOWEST_PRECEDENCE;
        }
    }
}
