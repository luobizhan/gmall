package com.lbz.gmall.payment.service.Impl;

import com.lbz.gmall.bean.PaymentInfo;
import com.lbz.gmall.payment.mapper.PaymentInfoMapper;
import com.lbz.gmall.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @author lbz
 * @create 2019-09-18 22:06
 */

@Service
@org.apache.dubbo.config.annotation.Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper ;
    @Autowired
    JmsTemplate jmsTemplate ;

    @Override
    public void addPaymentInfo(PaymentInfo paymentInfo) {

    }

    @Override
    public void editPaymentInfo(String orderId) {

        try {
            //更新交易信息
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setPaymentStatus("已支付");
            Example example = new Example(PaymentInfo.class);
            example.createCriteria().andEqualTo("orderId", orderId);
            //paymentInfoMapper.updateByExampleSelective(paymentInfo, example);
            //发送消息
            jmsTemplate.convertAndSend("PYMENT_SUCCESS_QUEUE",orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
