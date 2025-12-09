package com.lc.framework.web.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>
 *     为{@link MetaObjectHandler}实现类配置字段名，与实体类中的字段名对应
 * <pre/>
 * @author : Lu Cheng
 * @date : 9/12/25 15:32
 * @version : 1.0
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus.auto-fill-handler")
public class MetaObjectHandlerProperties {
    /**
     * 创建日期字段名
     */
    private String insertDateFieldName;

    /**
     * 创建人字段名
     */
    private String insertUserFieldName;

    /**
     * 更新日期字段名
     */
    private String updateDateFieldName;

    /**
     * 更新人字段名
     */
    private String updateUserFieldName;

}
