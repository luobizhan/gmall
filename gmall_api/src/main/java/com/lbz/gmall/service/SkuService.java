package com.lbz.gmall.service;

import com.lbz.gmall.bean.PmsSkuInfo;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-21 14:24
 */
public interface SkuService {
    /**
     * 保存skuInfo
     * @param pmsSkuInfo
     * @return
     */
    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    /**
     * 根据Id获取Sku的信息
     * @param skuId
     * @return
     */
    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkusFromSpu(String productId);

    /**
     * 根据catalog3Id查找所有sku信息
     * @param catalog3Id
     * @return
     */
    List<PmsSkuInfo> getAll(String catalog3Id);
}
