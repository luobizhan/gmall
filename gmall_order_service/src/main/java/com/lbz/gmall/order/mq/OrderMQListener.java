package com.lbz.gmall.order.mq;

import com.lbz.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * @author lbz
 * @create 2019-09-19 10:28
 */
@Component
public class OrderMQListener {

    @Autowired
    OrderService orderService ;

    @JmsListener(destination = "PYMENT_SUCCESS_QUEUE")
    public void listener(String orderId){

            if(orderId!=null){
                //收到支付成功的消息,更新订单消息
                orderService.updateOrderInfo(orderId);
            }

    }

}
