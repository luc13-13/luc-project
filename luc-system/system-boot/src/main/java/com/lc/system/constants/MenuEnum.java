package com.lc.system.constants;

import lombok.Getter;

/**
 * <pre>
 *     系统菜单枚举类型
 * <pre/>
 * @author : Lu Cheng
 * @date : 26/11/25 09:19
 * @version : 1.0
 */
public enum MenuEnum {
    CATALOG("catalog", "目录"),
    MENU("menu", "菜单"),
    BUTTON("button", "按钮"),
    EMBEDDED("embedded", "内嵌"),
    LINK("link", "外链");

    @Getter
    final String menuType;

    @Getter
    final String description;

    MenuEnum(String menuType, String description) {
        this.menuType = menuType;
        this.description = description;
    }

}
