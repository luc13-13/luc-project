package com.lc.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.framework.datascope.anno.DataScope;
import com.lc.framework.datascope.entity.DataScopeEntity;
import com.lc.framework.datascope.handler.SysUserDataScopeSqlHandler;
import com.lc.framework.datasource.starter.annotation.DataSourceSwitch;
import com.lc.system.domain.bo.OAuth2Profile;
import com.lc.system.domain.entity.SysUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2023/11/14 20:47
 * @version : 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserDO> {

    @DataScope(rules = SysUserDataScopeSqlHandler.class)
    @DataSourceSwitch("master")
    OAuth2Profile queryRolePermission(@Param("userId") String userId, @Param("dataScopeEntity") DataScopeEntity dataScopeEntity);
}
