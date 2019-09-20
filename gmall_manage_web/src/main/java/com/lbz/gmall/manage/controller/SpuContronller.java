package com.lbz.gmall.manage.controller;

import com.lbz.gmall.bean.PmsProductImage;
import com.lbz.gmall.bean.PmsProductInfo;
import com.lbz.gmall.bean.PmsProductSaleAttr;
import com.lbz.gmall.bean.PmsProductSaleAttrValue;
import com.lbz.gmall.manage.client.FastDfsClient;
import com.lbz.gmall.service.PmsProductService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author lbz
 * @create 2019-08-18 12:49
 */
@Controller
@CrossOrigin
public class SpuContronller {

    @Reference
    private PmsProductService pmsProductService;
    @Autowired
    private FastDfsClient fastDfsClient;

    /**
     * 返回所有的spu信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/spuList")
    public List<PmsProductInfo> spuList(String catalog3Id) {

        return pmsProductService.getSpuList(catalog3Id);
    }

    @ResponseBody
    @RequestMapping("/saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {

        String msg = pmsProductService.saveSpuInfo(pmsProductInfo);
        return msg;
    }

    /**
     * 处理文件上传请求
     *
     * @param multipartFile
     * @return
     */
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) {

        try {
            String url = fastDfsClient.uploadFile(multipartFile);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @ResponseBody
    @RequestMapping("/spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductService.getSpuSaleAttrList(spuId);
        return pmsProductSaleAttrs ;
    }

    @ResponseBody
    @RequestMapping("/spuImageList")
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> spuImageList = pmsProductService.getPmsProductImageList(spuId);
        return spuImageList ;
    }

}
