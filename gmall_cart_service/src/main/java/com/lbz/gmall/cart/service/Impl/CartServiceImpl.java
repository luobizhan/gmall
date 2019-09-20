package com.lbz.gmall.cart.service.Impl;

import com.alibaba.fastjson.JSON;
import com.lbz.gmall.bean.OmsCartItem;
import com.lbz.gmall.cart.mapper.OmsCartItemMapper;
import com.lbz.gmall.service.CartService;
import com.lbz.gmall.util.RedisUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
 * @author lbz
 * @create 2019-09-05 21:45
 */
@Service
@org.springframework.stereotype.Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OmsCartItemMapper omsCartItemMapper ;

    @Autowired
    RedisUtil redisUtil ;

    @Override
    public void saveOmsCartItems(OmsCartItem omsCartItem) {
            omsCartItemMapper.insertSelective(omsCartItem);
    }

    @Override
    public boolean ifExSku(String memberId, String skuId) {
       OmsCartItem omsCartItem = selectOmsBySkuIdAndMId(memberId,skuId);
        if(omsCartItem!=null){
            return true ;
        }
        return false;
    }

    public OmsCartItem selectOmsBySkuIdAndMId(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItemFromDB = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItemFromDB;
    }

    @Override
    public void updateOmsCartItem(OmsCartItem omsCartItem) {
        //先查DB
        OmsCartItem omsCartItemFromDB = selectOmsBySkuIdAndMId(omsCartItem.getMemberId(), omsCartItem.getProductSkuId());
        omsCartItem.setQuantity(omsCartItem.getQuantity().add(omsCartItemFromDB.getQuantity()));
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setId(omsCartItemFromDB.getId());
        omsCartItemMapper.updateByPrimaryKeySelective(omsCartItem);
    }

    @Override
    public void flushCache(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItemsFromDB = omsCartItemMapper.select(omsCartItem);
        Map<String, Object> map = new HashMap<>();
        if(omsCartItemsFromDB!=null&&omsCartItemsFromDB.size()>0){
            for (OmsCartItem omsCartItemFromDB : omsCartItemsFromDB) {
                map.put(omsCartItemFromDB.getId(), JSON.toJSONString(omsCartItemFromDB));
            }
        }
        boolean hasKey = redisUtil.hasKey("carts:" + memberId + ":info");
        if(hasKey){
            redisUtil.del("carts:" + memberId + ":info");
            redisUtil.hmset("carts:"+memberId+":info",map);
        }else {
            redisUtil.hmset("carts:"+memberId+":info",map);
        }
    }

    @Override
    public List<OmsCartItem> getCache(String memberId) {
        List<OmsCartItem> objects = new ArrayList<>();
        Map<Object, Object> hmget = redisUtil.hmget("carts:" + memberId + ":info");
        if(!hmget.isEmpty()) {
            for (Map.Entry<Object, Object> entry : hmget.entrySet()) {
                String value = (String) entry.getValue();
                OmsCartItem omsCartItem = JSON.parseObject(value, OmsCartItem.class);
                objects.add(omsCartItem);
            }
        }
        return objects;
    }

    @Override
    public void updateIsChecked(OmsCartItem omsCartItem) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("productSkuId",omsCartItem.getProductSkuId()).andEqualTo("memberId",omsCartItem.getMemberId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem, example);

        flushCache(omsCartItem.getMemberId());

    }

    @Override
    public List<OmsCartItem> getByMemberId(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
        return omsCartItems;
    }

    @Override
    public void deleteByChecked(OmsCartItem omsCartItem) {
        omsCartItemMapper.delete(omsCartItem);
    }

}
