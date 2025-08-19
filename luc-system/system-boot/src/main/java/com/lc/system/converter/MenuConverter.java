package com.lc.system.converter;

import com.lc.system.domain.bo.MenuBO;
import com.lc.system.domain.vo.MenuVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/16 11:34
 * @version : 1.0
 */
public interface MenuConverter {

    MenuVO convertBO2RouteVO(@NotNull MenuBO menuBO);

    List<MenuVO> convertBOList2VOTree(List<MenuBO> menuBOList);

    List<MenuVO> convertBOList2VOList(List<MenuBO> menuBOList);
}
