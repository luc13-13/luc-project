package com.lc.system.domain.bo;

import com.lc.system.domain.entity.MenuDO;
import com.lc.system.domain.entity.MenuMetaDO;
import lombok.Data;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/18 14:31
 * @version : 1.0
 */
@Data
public class MenuBO {

    private Long id;

    private String roleId;

    private MenuDO menuDO;

    private MenuMetaDO menuMetaDO;
}
