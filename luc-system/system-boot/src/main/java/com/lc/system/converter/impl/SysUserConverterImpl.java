package com.lc.system.converter.impl;

import com.lc.system.api.SysUserDetailDTO;
import com.lc.system.converter.SysUserConverter;
import com.lc.system.domain.entity.SysUserDO;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *     sys_user转换类
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/5 09:40
 * @version : 1.0
 */
@Service
public class SysUserConverterImpl implements SysUserConverter {

    @Override
    public SysUserDetailDTO convertDO2DetailDTO(SysUserDO sysUserDO) {
        return SysUserDetailDTO.builder()
                .userId(sysUserDO.getUserId())
                .userName(sysUserDO.getUserName())
                .deptId(sysUserDO.getDeptId())
                .nickName(sysUserDO.getNickName())
                .userType(sysUserDO.getUserType())
                .email(sysUserDO.getEmail())
                .phone(sysUserDO.getPhone())
                .sex(sysUserDO.getSex())
                .avatar(sysUserDO.getAvatar())
                .status(sysUserDO.getStatus())
                .password(sysUserDO.getPassword())
                .build();
    }
}
