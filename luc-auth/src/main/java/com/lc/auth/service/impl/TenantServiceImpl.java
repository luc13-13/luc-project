package com.lc.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.auth.domain.entity.Tenant;
import com.lc.auth.domain.security.TenantUserDetails;
import com.lc.auth.mapper.TenantMapper;
import com.lc.auth.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <pre>
 * 租户服务实现类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Tenant findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return baseMapper.findByUsername(username);
    }

    @Override
    public Tenant findByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        return baseMapper.findByPhone(phone);
    }

    @Override
    public Tenant findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return baseMapper.findByEmail(email);
    }

    @Override
    public Tenant findByTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            return null;
        }
        return baseMapper.findByTenantId(tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTenant(Tenant tenant) {
        if (tenant == null) {
            return false;
        }

        // 生成租户ID
        if (!StringUtils.hasText(tenant.getTenantId())) {
            tenant.setTenantId(generateTenantId());
        }

        // 加密密码
        if (StringUtils.hasText(tenant.getPassword())) {
            tenant.setPassword(passwordEncoder.encode(tenant.getPassword()));
        }

        // 设置默认值
        if (tenant.getStatus() == null) {
            tenant.setStatus(1); // 默认启用
        }
        if (tenant.getPhoneVerified() == null) {
            tenant.setPhoneVerified(0); // 默认未验证
        }
        if (tenant.getEmailVerified() == null) {
            tenant.setEmailVerified(0); // 默认未验证
        }
        if (tenant.getLoginCount() == null) {
            tenant.setLoginCount(0L);
        }

        return save(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tenant registerTenant(String username, String password, String phone, String email) {
        log.info("租户注册: username={}, phone={}, email={}", username, phone, email);

        // 参数验证
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password) || !StringUtils.hasText(phone)) {
            throw new IllegalArgumentException("用户名、密码和手机号不能为空");
        }

        // 检查重复
        if (existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (existsByPhone(phone)) {
            throw new IllegalArgumentException("手机号已存在");
        }
        if (StringUtils.hasText(email) && existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        // 创建租户
        Tenant tenant = new Tenant()
                .setTenantId(generateTenantId())
                .setUsername(username)
                .setPassword(password)
                .setPhone(phone)
                .setEmail(email)
                .setStatus(1)
                .setPhoneVerified(0)
                .setEmailVerified(0)
                .setLoginCount(0L);

        if (createTenant(tenant)) {
            log.info("租户注册成功: tenantId={}", tenant.getTenantId());
            return tenant;
        } else {
            throw new RuntimeException("租户注册失败");
        }
    }

    @Override
    public boolean validatePassword(Tenant tenant, String rawPassword) {
        if (tenant == null || !StringUtils.hasText(rawPassword) || !StringUtils.hasText(tenant.getPassword())) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, tenant.getPassword());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastLoginInfo(String tenantId, String loginIp) {
        if (!StringUtils.hasText(tenantId)) {
            return;
        }

        Tenant tenant = findByTenantId(tenantId);
        if (tenant != null) {
            tenant.setLastLoginTime(LocalDateTime.now())
                    .setLastLoginIp(loginIp)
                    .setLoginCount(tenant.getLoginCount() + 1);
            updateById(tenant);
            log.debug("更新租户登录信息: tenantId={}, loginIp={}", tenantId, loginIp);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return baseMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        return baseMapper.countByPhone(phone) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return baseMapper.countByEmail(email) > 0;
    }

    @Override
    public TenantUserDetails loadUserByUsername(String username) {
        Tenant tenant = findByUsername(username);
        if (tenant == null) {
            return null;
        }
        return new TenantUserDetails(tenant);
    }

    @Override
    public TenantUserDetails loadUserByPhone(String phone) {
        Tenant tenant = findByPhone(phone);
        if (tenant == null) {
            return null;
        }
        return new TenantUserDetails(tenant);
    }

    /**
     * 生成租户ID
     *
     * @return 租户ID
     */
    private String generateTenantId() {
        return "T" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
