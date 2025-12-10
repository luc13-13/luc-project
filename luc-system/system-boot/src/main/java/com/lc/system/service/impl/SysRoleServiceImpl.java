package com.lc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.framework.core.mvc.BizException;
import com.lc.framework.web.utils.WebUtil;
import com.lc.system.converter.SysRoleConverter;
import com.lc.system.domain.bo.SysRoleBO;
import com.lc.system.domain.dto.SysRoleDTO;
import com.lc.system.domain.entity.SysRoleDO;
import com.lc.system.domain.entity.SysRoleMenuDO;
import com.lc.system.domain.vo.RoleInfoVO;
import com.lc.system.mapper.SysRoleMapper;
import com.lc.system.mapper.SysRoleMenuMapper;
import com.lc.system.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            // 新增操作 - 检查角色ID是否已存在
            if (this.count(new LambdaQueryWrapper<SysRoleDO>()
                    .eq(SysRoleDO::getRoleId, dto.getRoleId())
                    .eq(SysRoleDO::getDeleted, false)) > 0) {
                throw BizException.exp("角色ID已存在: " + dto.getRoleId());
            }
            log.info("新增角色: roleId={}, roleName={}, operator={}", sysRoleDO.getRoleId(), sysRoleDO.getRoleName(), WebUtil.getUserId());
        }
        this.saveOrUpdate(sysRoleDO);

        // 处理角色-菜单关联关系（差异更新）
        updateRoleMenus(sysRoleDO.getRoleId(), dto.getMenuIds());

        return sysRoleDO.getRoleId();
    }

    /**
     * 差异更新角色菜单关联
     * 只删除被移除的，只插入新增的，保留未变化的记录
     */
    private void updateRoleMenus(String roleId, List<String> newMenuIds) {
        // 1. 获取当前已有的菜单ID
        SysRoleBO existedRole = baseMapper.selectRoleDetailWithMenus(roleId);

        // 2. 计算差异
        Set<String> existedSet = new HashSet<>(existedRole != null && !CollectionUtils.isEmpty(existedRole.getMenuIds()) ? existedRole.getMenuIds() : Collections.emptyList());
        Set<String> newSet = new HashSet<>(newMenuIds != null ? newMenuIds : Collections.emptyList());

        // 需要删除的 = 旧集合 - 新集合
        Set<String> toDelete = new HashSet<>(existedSet);
        toDelete.removeAll(newSet);

        // 需要插入的 = 新集合 - 旧集合
        Set<String> toInsert = new HashSet<>(newSet);
        toInsert.removeAll(existedSet);

        // 3. 执行删除
        if (!toDelete.isEmpty()) {
            sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenuDO>()
                    .eq(SysRoleMenuDO::getRoleId, roleId)
                    .in(SysRoleMenuDO::getMenuId, toDelete));
            log.info("删除角色菜单关联: roleId={}, menuIds={}", roleId, toDelete);
        }

        // 4. 执行插入
        if (!toInsert.isEmpty()) {
            for (String menuId : toInsert) {
                SysRoleMenuDO roleMenuDO = new SysRoleMenuDO();
                roleMenuDO.setRoleId(roleId);
                roleMenuDO.setMenuId(menuId);
                sysRoleMenuMapper.insert(roleMenuDO);
            }
            log.info("新增角色菜单关联: roleId={}, menuIds={}", roleId, toInsert);
        }
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
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenuDO>().eq(SysRoleMenuDO::getRoleId, roleId));
        log.info("删除角色: roleId={}, roleName={}, operator={}", roleDO.getRoleId(), roleDO.getRoleName(), WebUtil.getUserId());

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
