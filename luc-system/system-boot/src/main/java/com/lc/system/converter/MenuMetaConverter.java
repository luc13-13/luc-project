package com.lc.system.converter;

import com.lc.system.domain.dto.MenuDTO;
import com.lc.system.domain.entity.MenuMetaDO;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/3 16:43
 * @version : 1.0
 */
public interface MenuMetaConverter {

    MenuMetaDO convertMenuDTO2DO(MenuDTO menuDTO);
}
