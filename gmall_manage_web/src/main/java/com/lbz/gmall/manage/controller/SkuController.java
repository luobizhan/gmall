package com.lbz.gmall.manage.controller;

import com.lbz.gmall.bean.PmsSkuInfo;
import com.lbz.gmall.bean.PmsSkuSaleAttrValue;
import com.lbz.gmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-21 14:23
 */
@Controller
@CrossOrigin
public class SkuController {

    @Reference
    private SkuService skuService ;


    @ResponseBody
    @RequestMapping(value = "/saveSkuInfo",method = RequestMethod.POST)
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){

        String msg = skuService.saveSkuInfo(pmsSkuInfo);
        return msg ;
    }


}
