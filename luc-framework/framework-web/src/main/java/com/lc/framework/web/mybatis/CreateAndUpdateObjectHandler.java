package com.lc.framework.web.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lc.framework.web.utils.WebUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * <pre>
 *     自动填充创建时间、创建人、更新时间和更新人
 * <pre/>
 * @author : Lu Cheng
 * @date : 8/12/25 11:27
 * @version : 1.0
 */
public class CreateAndUpdateObjectHandler implements MetaObjectHandler {

    private final MetaObjectHandlerProperties handlerProperties;

    public CreateAndUpdateObjectHandler(MetaObjectHandlerProperties handlerProperties) {
        this.handlerProperties = handlerProperties;
        Assert.hasText(handlerProperties.getInsertDateFieldName(), "insertDateFieldName should not be empty");
        Assert.hasText(handlerProperties.getInsertUserFieldName(), "insertUserFieldName should not be empty");
        Assert.hasText(handlerProperties.getUpdateDateFieldName(), "updateDateFieldName should not be empty");
        Assert.hasText(handlerProperties.getUpdateUserFieldName(), "updateUserFieldName should not be empty");
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, handlerProperties.getInsertDateFieldName(), Date.class, new Date());
        this.strictInsertFill(metaObject, handlerProperties.getInsertUserFieldName(), String.class, WebUtil.getUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, handlerProperties.getUpdateDateFieldName(), Date.class, new Date());
        this.strictUpdateFill(metaObject, handlerProperties.getUpdateUserFieldName(), String.class, WebUtil.getUserId());
    }
}
