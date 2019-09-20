package com.lbz.gmall.order.service.Impl;

import com.lbz.gmall.bean.OmsCartItem;
import com.lbz.gmall.bean.OmsOrder;
import com.lbz.gmall.bean.OmsOrderItem;
import com.lbz.gmall.bean.PmsSkuInfo;
import com.lbz.gmall.order.mapper.OmsOrderItemMapper;
import com.lbz.gmall.order.mapper.OmsOrderMapper;
import com.lbz.gmall.service.CartService;
import com.lbz.gmall.service.OrderService;
import com.lbz.gmall.service.SkuService;
import com.lbz.gmall.util.RedisUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lbz
 * @create 2019-09-17 20:05
 */
@Service
@org.springframework.stereotype.Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Reference
    private SkuService skuService ;

    @Autowired
    OmsOrderMapper omsOrderMapper ;
    @Autowired
    OmsOrderItemMapper omsOrderItemMapper ;
    @Reference
    CartService cartService ;

    @Override
    public void saveCode(String code, String memberId) {
        redisUtil.set("memberId:" + memberId + ":code", code, 60 * 15);
    }

    @Override
    public boolean checkCode(String tradeCode, String memberId) {

        boolean checkCode = false;
        String codeFormCache = (String) redisUtil.get("memberId:" + memberId + ":code");
        if (codeFormCache!=null) {
            if (codeFormCache.equals(tradeCode)) {
                checkCode = true;
                //删除code 防止高并发可以用lua脚本删除
                redisUtil.del("memberId:" + memberId + ":code");
            }
        }
        return checkCode;
    }

    @Override
    public boolean checkPrice(BigDecimal price, String productSkuId) {

        boolean checkPrice = false ;
        //获取购物车中被结算的商品
        PmsSkuInfo skuInfo = skuService.getSkuById(productSkuId);
        BigDecimal dbPrice = new BigDecimal(String.valueOf(skuInfo.getPrice()));
        BigDecimal price2 = new BigDecimal(String.valueOf(price));
        String result = String.valueOf(price2.subtract(dbPrice));
        if(result.equals("0.00")){
            checkPrice = true ;
        }
        return checkPrice;
    }

    @Override
    public void addOrder(OmsOrder omsOrder) {
        //新增到order表中
        omsOrderMapper.insertSelective(omsOrder);
        //根据主键返回策略拿到Id
        String orderId = omsOrder.getId();
        //新增items
        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(omsOrderItem);
        }
        //删除选中的购物车商品
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setIsChecked("1");
        cartService.deleteByChecked(omsCartItem);

    }

    @Override
    public OmsOrder selectOrderByOrSn(String outTradeNo) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(outTradeNo);
        OmsOrder omsOrderFormDB = omsOrderMapper.selectOne(omsOrder);
        return omsOrderFormDB ;
    }

    @Override
    public void updateOrderInfo(String orderId) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setConfirmStatus(1);
        omsOrder.setModifyTime(new Date());

        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("id",orderId);
        omsOrderMapper.updateByExampleSelective(omsOrder,example);
    }

}
