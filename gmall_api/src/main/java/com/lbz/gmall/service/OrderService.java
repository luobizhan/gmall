package com.lbz.gmall.service;

import com.lbz.gmall.bean.OmsOrder;

import java.math.BigDecimal;

/**
 * @author lbz
 * @create 2019-09-17 18:22
 */
public interface OrderService {
    /**
     * 保存code到cache中
     * @param code
     * @param memberId
     */
    void saveCode(String code, String memberId);

    /**
     * 比较cache中的code与前端返回的code
     * @return
     * @param tradeCode
     * @param memberId
     */
    boolean checkCode(String tradeCode, String memberId);

    /**
     * 判断购物车价格与DB中商品价格是否一致
     * @param price
     * @param productSkuId
     * @return
     */
    boolean checkPrice(BigDecimal price, String productSkuId);

    /**
     * 新增订单
     * @param omsOrder
     */
    void addOrder(OmsOrder omsOrder);

    /**
     * 根据交易码查询订单
     * @param outTradeNo
     * @return
     */
    OmsOrder selectOrderByOrSn(String outTradeNo);

    /**
     * 更新订单
     * @param orderId
     */
    void updateOrderInfo(String orderId);
}
