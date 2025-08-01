package com.lc.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.auth.domain.entity.Tenant;
import com.lc.auth.domain.entity.TenantThirdParty;
import com.lc.auth.domain.security.TenantOAuth2User;
import com.lc.auth.mapper.TenantThirdPartyMapper;
import com.lc.auth.service.TenantService;
import com.lc.auth.service.TenantThirdPartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 租户第三方账号绑定服务实现类
 * </pre>
 *
 * @author Lu Cheng
 * @date 2025-08-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantThirdPartyServiceImpl extends ServiceImpl<TenantThirdPartyMapper, TenantThirdParty> implements TenantThirdPartyService {

    private final TenantService tenantService;
    private final ObjectMapper objectMapper;

    @Override
    public TenantThirdParty findByProviderAndUserId(String provider, String providerUserId) {
        if (!StringUtils.hasText(provider) || !StringUtils.hasText(providerUserId)) {
            return null;
        }
        return baseMapper.findByProviderAndUserId(provider, providerUserId);
    }

    @Override
    public List<TenantThirdParty> findByTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            return List.of();
        }
        return baseMapper.findByTenantId(tenantId);
    }

    @Override
    public TenantThirdParty findByTenantIdAndProvider(String tenantId, String provider) {
        if (!StringUtils.hasText(tenantId) || !StringUtils.hasText(provider)) {
            return null;
        }
        return baseMapper.findByTenantIdAndProvider(tenantId, provider);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantOAuth2User processThirdPartyLogin(String provider, OAuth2User oAuth2User) {
        log.info("处理第三方登录: provider={}, user={}", provider, oAuth2User.getName());

        String providerUserId = extractProviderUserId(provider, oAuth2User);
        if (!StringUtils.hasText(providerUserId)) {
            throw new IllegalArgumentException("无法获取第三方用户ID");
        }

        // 查找或创建第三方账号信息
        TenantThirdParty thirdParty = findByProviderAndUserId(provider, providerUserId);
        if (thirdParty == null) {
            thirdParty = createThirdPartyInfo(provider, oAuth2User);
            save(thirdParty);
        } else {
            // 更新第三方账号信息
            updateThirdPartyInfo(thirdParty, oAuth2User);
            updateById(thirdParty);
        }

        // 更新登录信息
        updateLastLoginInfo(provider, providerUserId);

        // 检查是否已绑定租户
        if (StringUtils.hasText(thirdParty.getTenantId())) {
            Tenant tenant = tenantService.findByTenantId(thirdParty.getTenantId());
            if (tenant != null) {
                log.info("第三方账号已绑定租户: provider={}, tenantId={}", provider, tenant.getTenantId());
                return new TenantOAuth2User(tenant, thirdParty, oAuth2User.getAttributes(), null, null);
            }
        }

        // 未绑定租户，返回未绑定的OAuth2用户
        log.info("第三方账号未绑定租户: provider={}, providerUserId={}", provider, providerUserId);
        return new TenantOAuth2User(thirdParty, oAuth2User.getAttributes());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindThirdPartyToTenant(String tenantId, String provider, OAuth2User oAuth2User) {
        log.info("绑定第三方账号到租户: tenantId={}, provider={}", tenantId, provider);

        if (!StringUtils.hasText(tenantId) || !StringUtils.hasText(provider) || oAuth2User == null) {
            return false;
        }

        String providerUserId = extractProviderUserId(provider, oAuth2User);
        if (!StringUtils.hasText(providerUserId)) {
            return false;
        }

        // 检查第三方账号是否已绑定其他租户
        if (isBoundToOtherTenant(provider, providerUserId, tenantId)) {
            throw new IllegalArgumentException("该第三方账号已绑定其他租户");
        }

        // 查找或创建第三方账号信息
        TenantThirdParty thirdParty = findByProviderAndUserId(provider, providerUserId);
        if (thirdParty == null) {
            thirdParty = createThirdPartyInfo(provider, oAuth2User);
        } else {
            updateThirdPartyInfo(thirdParty, oAuth2User);
        }

        // 绑定到租户
        thirdParty.setTenantId(tenantId)
                .setBindStatus(1)
                .setBindTime(LocalDateTime.now());

        return saveOrUpdate(thirdParty);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindThirdParty(String tenantId, String provider) {
        log.info("解绑第三方账号: tenantId={}, provider={}", tenantId, provider);

        TenantThirdParty thirdParty = findByTenantIdAndProvider(tenantId, provider);
        if (thirdParty == null) {
            return false;
        }

        thirdParty.setBindStatus(2)
                .setUnbindTime(LocalDateTime.now());

        return updateById(thirdParty);
    }

    @Override
    public boolean isBoundToOtherTenant(String provider, String providerUserId, String excludeTenantId) {
        if (!StringUtils.hasText(provider) || !StringUtils.hasText(providerUserId)) {
            return false;
        }
        String excludeId = StringUtils.hasText(excludeTenantId) ? excludeTenantId : "";
        return baseMapper.countBoundToOtherTenant(provider, providerUserId, excludeId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastLoginInfo(String provider, String providerUserId) {
        TenantThirdParty thirdParty = findByProviderAndUserId(provider, providerUserId);
        if (thirdParty != null) {
            thirdParty.setLastLoginTime(LocalDateTime.now())
                    .setLoginCount(thirdParty.getLoginCount() + 1);
            updateById(thirdParty);
        }
    }

    @Override
    public TenantThirdParty createThirdPartyInfo(String provider, OAuth2User oAuth2User) {
        String providerUserId = extractProviderUserId(provider, oAuth2User);
        
        TenantThirdParty thirdParty = new TenantThirdParty()
                .setProvider(provider)
                .setProviderUserId(providerUserId)
                .setProviderUsername(extractProviderUsername(provider, oAuth2User))
                .setProviderNickname(extractProviderNickname(provider, oAuth2User))
                .setProviderAvatar(extractProviderAvatar(provider, oAuth2User))
                .setProviderEmail(extractProviderEmail(provider, oAuth2User))
                .setBindStatus(0)
                .setLoginCount(0L);

        // 保存原始信息
        try {
            thirdParty.setProviderRawInfo(objectMapper.writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            log.warn("序列化第三方用户信息失败", e);
        }

        return thirdParty;
    }

    /**
     * 更新第三方账号信息
     */
    private void updateThirdPartyInfo(TenantThirdParty thirdParty, OAuth2User oAuth2User) {
        thirdParty.setProviderUsername(extractProviderUsername(thirdParty.getProvider(), oAuth2User))
                .setProviderNickname(extractProviderNickname(thirdParty.getProvider(), oAuth2User))
                .setProviderAvatar(extractProviderAvatar(thirdParty.getProvider(), oAuth2User))
                .setProviderEmail(extractProviderEmail(thirdParty.getProvider(), oAuth2User));

        try {
            thirdParty.setProviderRawInfo(objectMapper.writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            log.warn("序列化第三方用户信息失败", e);
        }
    }

    /**
     * 提取第三方平台用户ID
     */
    private String extractProviderUserId(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return switch (provider.toLowerCase()) {
            case "gitee" -> (String) attributes.get("id");
            case "wechat" -> (String) attributes.get("openid");
            case "github" -> String.valueOf(attributes.get("id"));
            default -> oAuth2User.getName();
        };
    }

    /**
     * 提取第三方平台用户名
     */
    private String extractProviderUsername(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return switch (provider.toLowerCase()) {
            case "gitee" -> (String) attributes.get("login");
            case "wechat" -> (String) attributes.get("nickname");
            case "github" -> (String) attributes.get("login");
            default -> oAuth2User.getName();
        };
    }

    /**
     * 提取第三方平台用户昵称
     */
    private String extractProviderNickname(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return switch (provider.toLowerCase()) {
            case "gitee" -> (String) attributes.get("name");
            case "wechat" -> (String) attributes.get("nickname");
            case "github" -> (String) attributes.get("name");
            default -> (String) attributes.get("name");
        };
    }

    /**
     * 提取第三方平台用户头像
     */
    private String extractProviderAvatar(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return switch (provider.toLowerCase()) {
            case "gitee" -> (String) attributes.get("avatar_url");
            case "wechat" -> (String) attributes.get("headimgurl");
            case "github" -> (String) attributes.get("avatar_url");
            default -> (String) attributes.get("avatar_url");
        };
    }

    /**
     * 提取第三方平台用户邮箱
     */
    private String extractProviderEmail(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return (String) attributes.get("email");
    }
}
