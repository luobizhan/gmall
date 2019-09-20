package com.lbz.gmall.manage.controller;

import com.lbz.gmall.bean.PmsBaseAttrInfo;
import com.lbz.gmall.bean.PmsBaseAttrValue;
import com.lbz.gmall.bean.PmsBaseSaleAttr;
import com.lbz.gmall.service.PmsBaseAttrService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 18:07
 */
@CrossOrigin
@Controller
public class AtrrInfoController {

    @Reference
    private PmsBaseAttrService pmsBaseAttrService;

    /**
     * 获取请求参数catalog3Id 查找三级分类的所有信息
     * @param catalog3Id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/attrInfoList")
    public List<PmsBaseAttrInfo> getAtrrInfoList(String catalog3Id){

        return pmsBaseAttrService.getAtrrInfoList(catalog3Id);
    }

    /**
     * 获取属性Id 查找对应属性的值
     * @param attrId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAtrrValueList(String attrId){

        return pmsBaseAttrService.getAtrrValueList(attrId);

    }

    /**
     * 把请求中的json数据封装到pmsbaseattrinfo对象中
     * 保存到数据库
     * @param pmsBaseAttrInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){

        String msg = pmsBaseAttrService.saveAttrInfo(pmsBaseAttrInfo);
        return msg;
    }

    /**
     * 返回pmsBaseAttrSale 表中的所有值
     * @return
     */
    @ResponseBody
    @RequestMapping("/baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList (){

        return pmsBaseAttrService.getBaseSaleAttrList() ;
    }

}
