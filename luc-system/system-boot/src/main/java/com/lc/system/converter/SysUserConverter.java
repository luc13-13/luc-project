package com.lc.system.converter;

import com.lc.system.api.SysUserDetailDTO;
import com.lc.system.domain.entity.SysUserDO;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/5 09:40
 * @version : 1.0
 */
public interface SysUserConverter {
    SysUserDetailDTO convertDO2DetailDTO(SysUserDO sysUserDO);
}
