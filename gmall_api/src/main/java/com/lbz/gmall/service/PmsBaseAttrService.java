package com.lbz.gmall.service;

import com.lbz.gmall.bean.PmsBaseAttrInfo;
import com.lbz.gmall.bean.PmsBaseAttrValue;
import com.lbz.gmall.bean.PmsBaseSaleAttr;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 18:16
 */
public interface PmsBaseAttrService {

    List<PmsBaseAttrInfo> getAtrrInfoList(String catalogId3) ;

    List<PmsBaseAttrValue> getAtrrValueList(String attrId);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseSaleAttr> getBaseSaleAttrList();

    /**
     * 根据valueId的字符串查到对应的平台属性的信息
     * @param parm
     * @return
     */
    List<PmsBaseAttrInfo> getAtrrInfoListByValueId(String parm);

    /**
     * 获取不重复的AttrName
     * @return
     */
    List<PmsBaseAttrInfo> getAtrrNameList(String parm);
}
