package com.lc.framework.web.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * <pre>
 *     自动填充创建时间和更新时间
 * <pre/>
 * @author : Lu Cheng
 * @date : 8/12/25 11:27
 * @version : 1.0
 * @param insertFieldName  创建时间字段名
 * @param updateFieldName  更新时间字段名
 */
public record CreateAndUpdateObjectHandler(String insertFieldName,
                                           String updateFieldName) implements MetaObjectHandler {

    public CreateAndUpdateObjectHandler {
        Assert.hasText(insertFieldName, "insertFieldName should not be empty");
        Assert.hasText(updateFieldName, "updateFieldName should not be empty");
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, insertFieldName, Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, updateFieldName, Date.class, new Date());
    }
}
