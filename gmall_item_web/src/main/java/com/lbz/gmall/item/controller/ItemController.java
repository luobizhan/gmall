package com.lbz.gmall.item.controller;

import com.alibaba.fastjson.JSON;
import com.lbz.gmall.bean.PmsProductSaleAttr;
import com.lbz.gmall.bean.PmsSkuInfo;
import com.lbz.gmall.bean.PmsSkuSaleAttrValue;
import com.lbz.gmall.service.PmsProductService;
import com.lbz.gmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lbz
 * @create 2019-08-22 16:50
 */

@Controller
public class ItemController {

    @Reference
    private SkuService skuService ;
    @Reference
    private PmsProductService pmsProductService ;

    @RequestMapping("/{id}")
    public String test(@PathVariable("id") String skuId, Model model){

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        model.addAttribute("skuInfo",pmsSkuInfo);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductService.spuSaleAttrListCheckBySku(skuId,pmsSkuInfo.getProductId());
        model.addAttribute("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        //获取sku对应的spu中所有sku的id和sku销售属性值id的hash表
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkusFromSpu(pmsSkuInfo.getProductId());
        Map<String,String> map = new HashMap<>();

        for (PmsSkuInfo sku: pmsSkuInfos) {

            String key = "" ;
            String value = sku.getId() ;

            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : sku.getSkuSaleAttrValueList()){
                key+= pmsSkuSaleAttrValue.getSaleAttrValueId()+"|" ;
            }

            map.put(key,value);
        }
        //讲hash表放到页面
        String jsonString = JSON.toJSONString(map);
        model.addAttribute("hashJson",jsonString);
        return "item" ;
    }

    @ResponseBody
    @RequestMapping("/getAllSku")
    public List<PmsSkuInfo> getAllSku(){
        List<PmsSkuInfo> all = skuService.getAll("61");
        return all ;
    }

}
