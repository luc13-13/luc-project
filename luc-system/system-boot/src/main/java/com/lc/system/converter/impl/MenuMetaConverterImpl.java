package com.lc.system.converter.impl;

import com.lc.system.converter.MenuMetaConverter;
import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.dto.MenuMetaDTO;
import com.lc.system.domain.entity.MenuMetaDO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/3 16:43
 * @version : 1.0
 */
@Service
public class MenuMetaConverterImpl implements MenuMetaConverter {
    @Override
    public MenuMetaDO convertMenuDTO2DO(MenuDTO menuDTO) {
        MenuMetaDO metaDO = this.convertDTO2DO(menuDTO.getMeta());
        metaDO.setMenuId(menuDTO.getMenuId());
        return metaDO;
    }

    public MenuMetaDO convertDTO2DO(MenuMetaDTO metaDTO) {
        MenuMetaDO metaDO = new MenuMetaDO();
        metaDO.setTitle(metaDTO.getTitle());
        metaDO.setIcon(StringUtils.hasText(metaDTO.getIcon()) ? metaDTO.getIcon() : null);
        metaDO.setActiveIcon(StringUtils.hasText(metaDTO.getActiveIcon()) ? metaDTO.getActiveIcon() : null);
        metaDO.setActivePath(StringUtils.hasText(metaDTO.getActivePath()) ? metaDTO.getActivePath() : null);
        metaDO.setAuthority(StringUtils.hasText(metaDTO.getAuthority()) ? metaDTO.getAuthority() : null);
        metaDO.setIgnoreAccess(metaDTO.getIgnoreAccess());
        metaDO.setMenuVisibleWithForbidden(metaDTO.getMenuVisibleWithForbidden());
        metaDO.setHideInMenu(metaDTO.getHideInMenu());
        metaDO.setHideInTab(metaDTO.getHideInTab());
        metaDO.setHideInBreadcrumb(metaDTO.getHideInBreadcrumb());
        metaDO.setHideChildrenInMenu(metaDTO.getHideChildrenInMenu());
        metaDO.setAffixTab(metaDTO.getAffixTab());
        metaDO.setAffixTabOrder(metaDTO.getAffixTabOrder());
        metaDO.setMaxNumOfOpenTab(metaDTO.getMaxNumOfOpenTab());
        metaDO.setKeepAlive(metaDTO.getKeepAlive());
        metaDO.setNoBasicLayout(metaDTO.getNoBasicLayout());
        metaDO.setLink(StringUtils.hasText(metaDTO.getLink()) ? metaDTO.getLink() : null);
        metaDO.setIframeSrc(StringUtils.hasText(metaDTO.getIframeSrc()) ? metaDTO.getIframeSrc() : null);
        metaDO.setOpenInNewWindow(metaDTO.getOpenInNewWindow());
        metaDO.setBadge(StringUtils.hasText(metaDTO.getBadge()) ? metaDTO.getBadge() : null);
        metaDO.setBadgeType(StringUtils.hasText(metaDTO.getBadgeType()) ? metaDTO.getBadgeType() : null);
        metaDO.setBadgeVariants(StringUtils.hasText(metaDTO.getBadgeVariants()) ? metaDTO.getBadgeVariants() : null);
        metaDO.setQueryParams(StringUtils.hasText(metaDTO.getQueryParams()) ? metaDTO.getQueryParams() : null);
        return  metaDO;
    }
}
