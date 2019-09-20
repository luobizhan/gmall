package com.lbz.gmall.manage.mapper;

import com.lbz.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-21 18:17
 */
public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    /**
     * 根据productId查找所有的sku和sku对应的销售属性集合
     * @return
     */
    List<PmsSkuInfo> selectSkusFromSkuInfoByProductId(@Param("productId") String productId);

}
