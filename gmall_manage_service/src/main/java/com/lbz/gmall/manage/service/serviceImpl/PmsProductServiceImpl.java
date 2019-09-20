package com.lbz.gmall.manage.service.serviceImpl;

import com.lbz.gmall.bean.*;
import com.lbz.gmall.manage.mapper.PmsProductImageMapper;
import com.lbz.gmall.manage.mapper.PmsProductInfoMapper;
import com.lbz.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.lbz.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.lbz.gmall.service.PmsProductService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-18 12:58
 */
@Service
@org.springframework.stereotype.Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper ;
    @Autowired
    private PmsProductImageMapper pmsProductImageMapper ;
    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper ;
    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper ;

    @Override
    public List<PmsProductInfo> getSpuList(String catalog3Id) {

        Example example = new Example(PmsProductInfo.class);
        example.createCriteria().andEqualTo("catalog3Id",catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.selectByExample(example);

        return pmsProductInfos;
    }

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        String msg = null;

        try {
            //新增pmsProductInfo 信息
            msg = "success";
            int result = pmsProductInfoMapper.insertSelective(pmsProductInfo) ;

            //新增 pmsProductImage信息
            List<PmsProductImage> images = pmsProductInfo.getSpuImageList() ;
            for (PmsProductImage image: images) {
                //根据主键返回策略添加id值
                image.setProductId(pmsProductInfo.getId());
                pmsProductImageMapper.insertSelective(image) ;
            }

            //从pmsProductInfo中取出pmsProductAttr信息
            List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfo.getSpuSaleAttrList();

            //取出封装在pmsProductSaleAttr中的 value集合,然后执行新增操作

            for (PmsProductSaleAttr saleAttr: pmsProductSaleAttrList) {
                //新增pmsProductAttr信息
                saleAttr.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrMapper.insertSelective(saleAttr) ;

                //取出PmsProductSaleAttrValue集合
                List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                for (PmsProductSaleAttrValue saleAttrValue:pmsProductSaleAttrValueList) {

                    saleAttrValue.setProductId(pmsProductInfo.getId());
                    //新增pmsProductSaleAttrValue 信息
                    pmsProductSaleAttrValueMapper.insertSelective(saleAttrValue) ;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = "error" ;
        }
        return msg;
    }

    @Override
    public List<PmsProductSaleAttrValue> getSpuSaleAttrValueList(String saleAttrId,String spuId) {

        PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
        pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
        pmsProductSaleAttrValue.setProductId(spuId);
        List<PmsProductSaleAttrValue> saleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

        return saleAttrValueList;
    }

    @Override
    public List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId) {

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> saleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for(PmsProductSaleAttr spu:saleAttrList){
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = getSpuSaleAttrValueList(spu.getSaleAttrId(),spu.getProductId());
            spu.setSpuSaleAttrValueList(spuSaleAttrValueList);
        }
        return saleAttrList;
    }

    @Override
    public List<PmsProductImage> getPmsProductImageList(String spuId) {

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);

        List<PmsProductImage> productImages = pmsProductImageMapper.select(pmsProductImage);
        return productImages;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String skuId, String productId) {

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.spuSaleAttrListCheckBySku(skuId,productId);
        return pmsProductSaleAttrs;
    }
}
