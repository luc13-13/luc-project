package com.lc.framework.storage.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;
import static com.lc.framework.storage.core.StorageProperties.PREFIX;
import com.lc.framework.storage.core.StorageConstants.StorageTypeEnum;
/**
 * <pre>
 *     存储工具属性
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:11
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class StorageProperties {

    public static final String PREFIX = "file.storage";

    private boolean enabled = true;

    /**
     * 存储类型<br/>{@link StorageTypeEnum#OSS_QINIU}<br/>{@link StorageTypeEnum#OSS_LOCAL}
     */
    private String type;

    /**
     * SK
     */
    private String secretKey;

    /**
     * AK
     */
    private String accessKey;

    /**
     * 存储引擎实现类特有的属性， 通过
     */
    private Map<String, Object> properties;
}
