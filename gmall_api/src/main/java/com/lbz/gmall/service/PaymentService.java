package com.lbz.gmall.service;

import com.lbz.gmall.bean.PaymentInfo;

/**
 * @author lbz
 * @create 2019-09-18 22:05
 */
public interface PaymentService {
    /**
     * 新增交易信息表
     * @param paymentInfo
     */
    void addPaymentInfo(PaymentInfo paymentInfo);

    /**
     * 修改支付信息
     * @param orderId
     */
    void editPaymentInfo(String orderId);
}
