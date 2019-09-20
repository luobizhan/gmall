package com.lbz.gmall.manage.service.serviceImpl;

import com.lbz.gmall.bean.*;
import com.lbz.gmall.manage.mapper.*;
import com.lbz.gmall.service.SkuService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-21 18:15
 */
@Service
@org.apache.dubbo.config.annotation.Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper ;
    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper ;
    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper ;
    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    private RedissonClient redissonClient ;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        String msg = "success" ;
        try {
            //保存pmsSkuInfo(spuId是为了映射前段接口而设置的一个临时属性)
            pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
            pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

            //保存pmsSkuAttrValue
            List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
            for(PmsSkuAttrValue psav:skuAttrValueList){
                //主键返回策略获取skuId
                psav.setSkuId(pmsSkuInfo.getId());
                pmsSkuAttrValueMapper.insertSelective(psav) ;
            }

            //保存pmsSkuSaleAttrValue
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            for(PmsSkuSaleAttrValue pssav:skuSaleAttrValueList){
                //主键返回策略获取skuId
                pssav.setSkuId(pmsSkuInfo.getId());
                pmsSkuSaleAttrValueMapper.insertSelective(pssav);
            }

            //保存pmsSkuImage
            List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();

            for(PmsSkuImage pmsSkuImage:skuImageList){
                //主键返回策略获取skuId
                pmsSkuImage.setSkuId(pmsSkuInfo.getId());
                //获取productImageId(spuImageId是为了映射前段接口而设置的一个临时属性)
                pmsSkuImage.setProductImgId(pmsSkuImage.getSpuImgId());
                pmsSkuImageMapper.insertSelective(pmsSkuImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "error" ;
        }
        return msg;
    }

    @Cacheable(value = "skuInfo",key = "'sku:'+#skuId+':Info'")
    @Override
    public PmsSkuInfo getSkuById(String skuId) {

        //在进入数据库查找前，在上一层锁，防止缓存击穿
        //RLock lock = redissonClient.getLock("lock");
        //lock.lock();
        try {
            //获取sku表信息
            PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
            pmsSkuInfo.setId(skuId);
            PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

            //获取skuImageList
            PmsSkuImage pmsSkuImage = new PmsSkuImage();
            pmsSkuImage.setSkuId(skuId);
            List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
            skuInfo.setSkuImageList(pmsSkuImages);
            return skuInfo;
        } finally {
            //开锁
            //lock.unlock();
        }
    }

    @Override
    public List<PmsSkuInfo> getSkusFromSpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectSkusFromSkuInfoByProductId(productId);
        return pmsSkuInfoList;
    }

    @Override
    public List<PmsSkuInfo> getAll(String catalog3Id) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        if(catalog3Id != null){
            pmsSkuInfo.setCatalog3Id(catalog3Id);
        }
        List<PmsSkuInfo> skuInfoList = pmsSkuInfoMapper.select(pmsSkuInfo);

        PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
        for (PmsSkuInfo skuInfo : skuInfoList) {
            pmsSkuAttrValue.setSkuId(skuInfo.getId());
            List<PmsSkuAttrValue> skuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            skuInfo.setSkuAttrValueList(skuAttrValues);
        }

        return skuInfoList;
    }
}
