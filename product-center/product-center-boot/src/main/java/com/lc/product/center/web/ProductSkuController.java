package com.lc.product.center.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.core.page.PaginationResult;
import com.lc.framework.core.utils.validator.Groups;
import com.lc.product.center.domain.dto.ProductSkuDTO;
import com.lc.product.center.domain.vo.ProductSkuDetailsVO;
import com.lc.product.center.domain.vo.ProductSkuVO;
import com.lc.product.center.service.ProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品SKU管理控制器
 *
 * @author lucheng
 * @since 2026-01-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/sku")
@Tag(name = "产品SKU管理", description = "产品SKU的增删改查接口")
public class ProductSkuController {

    private final ProductSkuService productSkuService;

    @PostMapping("/list")
    @Operation(summary = "查询SKU列表")
    public WebResult<List<ProductSkuVO>> list(@RequestBody ProductSkuDTO queryDTO) {
        List<ProductSkuVO> list = productSkuService.listByCondition(queryDTO);
        return WebResult.success(list);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询SKU列表")
    public WebResult<PaginationResult<ProductSkuVO>> page(
            @RequestBody @Validated(Groups.PageGroup.class) ProductSkuDTO queryDTO) {
        PaginationResult<ProductSkuVO> result = productSkuService.pageByCondition(queryDTO);
        return WebResult.success(result);
    }

    @GetMapping("/detail")
    @Operation(summary = "查询SKU详情")
    public WebResult<ProductSkuDetailsVO> detail(ProductSkuDTO skuDTO) {
        ProductSkuDetailsVO vo = productSkuService.getDetail(skuDTO);
        return WebResult.success(vo);
    }

    @PostMapping("/create")
    @Operation(summary = "创建产品SKU")
    public WebResult<ProductSkuVO> create(
            @RequestBody @Validated(Groups.AddGroup.class) ProductSkuDTO skuDTO) {
        ProductSkuVO vo = productSkuService.createSku(skuDTO);
        return WebResult.success(vo);
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品SKU")
    public WebResult<ProductSkuVO> update(
            @RequestBody @Validated(Groups.UpdateGroup.class) ProductSkuDTO skuDTO) {
        ProductSkuVO vo = productSkuService.updateSku(skuDTO);
        return WebResult.success(vo);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除产品SKU")
    public WebResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = productSkuService.deleteSku(id);
        return WebResult.success(result);
    }


    @DeleteMapping("/delete/item")
    @Operation(summary = "删除产品SKU计费项")
    public WebResult<Boolean> deleteItem() {
        return WebResult.success();
    }
}
