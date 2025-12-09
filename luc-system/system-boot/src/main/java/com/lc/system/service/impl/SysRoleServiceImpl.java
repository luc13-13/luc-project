package com.lc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.framework.core.utils.validator.ValidatorUtil;
import com.lc.framework.web.utils.WebUtil;
import com.lc.system.converter.SysRoleConverter;
import com.lc.system.domain.bo.SysRoleBO;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.entity.SysRoleMenuDO;
import com.lc.system.domain.vo.RoleInfoVO;
import com.lc.system.mapper.SysRoleMapper;
import com.lc.system.mapper.SysRoleMenuMapper;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统角色表(luc_system.sys_role)表服务实现类
 *
 * @author lucheng
 * @since 2025-09-04
 */
@Slf4j
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<RoleInfoVO> getRoleList() {
        // 一次查询获取角色列表及关联的菜单ID
        List<SysRoleBO> roleBOList = baseMapper.selectRoleListWithMenus();
        return sysRoleConverter.convertBO2VO(roleBOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRole(SysRoleDTO dto) {
        // 使用 converter 转换 DTO 为 DO
        SysRoleDO sysRoleDO = sysRoleConverter.convertDTO2DO(dto);

        if (sysRoleDO.getId() == null) {
            ValidatorUtil.validate(dto, Groups.AddGroup.class);
            // 新增操作 - 检查角色ID是否已存在
            if (StringUtils.hasText(dto.getRoleId())) {
                LambdaQueryWrapper<SysRoleDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SysRoleDO::getRoleId, dto.getRoleId())
                        .eq(SysRoleDO::getDeleted, false);
                if (this.count(wrapper) > 0) {
                    throw BizException.exp("角色ID已存在: " + dto.getRoleId());
                }
            }
            sysRoleDO.setDeleted(false);
            log.info("新增角色: roleId={}, roleName={}, operator={}",
                    sysRoleDO.getRoleId(), sysRoleDO.getRoleName(), WebUtil.getUserId());
        } else {
            ValidatorUtil.validate(dto, Groups.UpdateGroup.class);
        }
        this.saveOrUpdate(sysRoleDO);

        // 处理角色-菜单关联关系
        String roleId = sysRoleDO.getRoleId();
        if (StringUtils.hasText(roleId)) {
            // 先删除原有的关联关系
            LambdaQueryWrapper<SysRoleMenuDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(SysRoleMenuDO::getRoleId, roleId);
            sysRoleMenuMapper.delete(deleteWrapper);

            // 插入新的关联关系
            List<String> menuIds = dto.getMenuIds();
            if (!CollectionUtils.isEmpty(menuIds)) {
                String userId = WebUtil.getUserId();
                for (String menuId : menuIds) {
                    SysRoleMenuDO roleMenuDO = new SysRoleMenuDO();
                    roleMenuDO.setRoleId(roleId);
                    roleMenuDO.setMenuId(menuId);
                    roleMenuDO.setCreatedBy(userId);
                    sysRoleMenuMapper.insert(roleMenuDO);
                }
                log.info("保存角色菜单关联: roleId={}, menuIds={}", roleId, menuIds);
            }
        }

        return sysRoleDO.getRoleId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteRole(String roleId) {

        // 查询角色是否存在
        SysRoleDO roleDO = this.getOne(new LambdaQueryWrapper<SysRoleDO>().eq(SysRoleDO::getRoleId, roleId)
                .eq(SysRoleDO::getDeleted, false));
        if (roleDO == null) {
            throw BizException.exp("角色不存在或已被删除: " + roleId);
        }

        // 逻辑删除角色
        roleDO.setDeleted(true);
        this.updateById(roleDO);

        // 删除角色菜单关联关系
        LambdaQueryWrapper<SysRoleMenuDO> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysRoleMenuDO::getRoleId, roleId);
        sysRoleMenuMapper.delete(deleteWrapper);

        log.info("删除角色: roleId={}, roleName={}, operator={}",
                roleDO.getRoleId(), roleDO.getRoleName(), WebUtil.getUserId());

        return roleId;
    }

    @Override
    public RoleInfoVO getRoleDetails(String roleId) {
        // 一次查询获取角色详情及关联的菜单ID
        SysRoleBO roleBO = baseMapper.selectRoleDetailWithMenus(roleId);
        if (roleBO == null) {
            throw BizException.exp("角色不存在或已被删除: " + roleId);
        }

        return sysRoleConverter.convertBO2VO(roleBO);
    }
}
