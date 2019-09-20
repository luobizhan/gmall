package com.lbz.gmall.service;

import com.lbz.gmall.bean.PmsProductImage;
import com.lbz.gmall.bean.PmsProductInfo;
import com.lbz.gmall.bean.PmsProductSaleAttr;
import com.lbz.gmall.bean.PmsProductSaleAttrValue;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-18 12:52
 */
public interface PmsProductService {

    /**
     * select by catalog3Id for pmsProductInfo
     * @param catalog3Id
     * @return
     */
    List<PmsProductInfo> getSpuList(String catalog3Id);

    /**
     * 保存pmsProductInfo 信息
     * @param pmsProductInfo
     * @return
     */
    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    /**
     *
     * 获取spuSaleArrtValue的所有值
     * @param saleAttrId
     * @return
     */
    List<PmsProductSaleAttrValue> getSpuSaleAttrValueList(String saleAttrId,String spuId);

    /**
     * 获取spuSaleAttr的集合包括value值
     * @param spuId
     * @return
     */
    List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId);

    /**
     * 返回所有的spu图片信息
     * @param spuId
     * @return
     */
    List<PmsProductImage> getPmsProductImageList(String spuId);

    /**
     * 返回spu的销售属性和销售属性的值，并添加ischecked字段来判断是否是传入是skuId的属性
     * @param skuId
     * @param prductId
     * @return
     */
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String skuId, String prductId);
}
