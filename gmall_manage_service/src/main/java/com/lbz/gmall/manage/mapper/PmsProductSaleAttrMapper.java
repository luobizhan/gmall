package com.lbz.gmall.manage.mapper;

import com.lbz.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-21 15:59
 */
public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@Param("skuId") String skuId,@Param("productId") String productId);
}
