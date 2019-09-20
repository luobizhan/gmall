package com.lbz.gmall.manage.service.serviceImpl;

import com.lbz.gmall.bean.PmsBaseAttrInfo;
import com.lbz.gmall.bean.PmsBaseAttrValue;
import com.lbz.gmall.bean.PmsBaseSaleAttr;
import com.lbz.gmall.bean.PmsProductInfo;
import com.lbz.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.lbz.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.lbz.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.lbz.gmall.service.PmsBaseAttrService;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 18:17
 */

@Service
@org.apache.dubbo.config.annotation.Service
public class PmsBaseAttrInfoServiceImpl implements PmsBaseAttrService {
    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper ;
    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper ;
    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper ;

    @Override
    public List<PmsBaseAttrInfo> getAtrrInfoList(String catalogId3) {
        //new一个对象用属性值来作为查询条件，
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();

        pmsBaseAttrInfo.setCatalog3Id(catalogId3);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        //new一个对象用属性值来作为查询条件，
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        for(PmsBaseAttrInfo pmsBaseAttrInfo1:pmsBaseAttrInfos){
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo1.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            pmsBaseAttrInfo1.setAttrValueList(pmsBaseAttrValues);
        }

        return pmsBaseAttrInfos;
    }

    /**
     * 查找attrid 的属性值
     * @param attrId
     * @return
     */
    @Override
    public List<PmsBaseAttrValue> getAtrrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }

    /**
     * 新增attrinfo和value
     * 成功 返回 success
     * @param pmsBaseAttrInfo
     * @return
     */
    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        String msg = "success" ;

        //如果 pmsBaseAttrInfo的Id值为null 就是新增操作，否则为更新操作
        if(StringUtil.isBlank(pmsBaseAttrInfo.getId())){

            //新增操作
            try {
                //添加attrInfo
                pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo) ;

                //遍历插入attrValue
                List<PmsBaseAttrValue> results = pmsBaseAttrInfo.getAttrValueList();
                for (PmsBaseAttrValue pmsAttrValue:results) {
                    //通过主键返回策略设置atrrValue的attrId值
                    pmsAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                    pmsBaseAttrValueMapper.insertSelective(pmsAttrValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = "error" ;
            }
            return msg;

        }else{

            try {
                //更新操作

                //更新attrInfo
                Example example = new Example(PmsBaseAttrInfo.class);
                //添加更新条件
                example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId()) ;
                pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example) ;

                //取出attrValue集合
                List<PmsBaseAttrValue> resultsForDel = pmsBaseAttrInfo.getAttrValueList();

                //删除所属属性的值
                Example example1 = new Example(PmsBaseAttrValue.class);
                example1.createCriteria().andEqualTo("attrId",pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.deleteByExample(example1);

                for (PmsBaseAttrValue pmsBaseAttrValueDel:resultsForDel) {

                    //给AttrValue的Id 赋值 null,然后新增操作
                    pmsBaseAttrValueDel.setId(null);
                    //设置attr_id值
                    pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
                    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValueDel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = "error" ;
            }

            return msg ;
        }

    }

    @Override
    public List<PmsBaseSaleAttr> getBaseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAtrrInfoListByValueId(String parm) {
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoMapper.getAtrrInfoListByValueId(parm);
        return pmsBaseAttrInfoList;
    }

    @Override
    public List<PmsBaseAttrInfo> getAtrrNameList(String parm) {
        List<PmsBaseAttrInfo> attrNameList = pmsBaseAttrInfoMapper.getAtrrNameList(parm);

        return attrNameList;
    }
}
