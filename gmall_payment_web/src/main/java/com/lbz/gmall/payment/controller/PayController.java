package com.lbz.gmall.payment.controller;

import com.lbz.gmall.bean.OmsOrder;
import com.lbz.gmall.bean.PaymentInfo;
import com.lbz.gmall.service.OrderService;
import com.lbz.gmall.service.PaymentService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lbz
 * @create 2019-09-18 21:17
 */
@Controller
public class PayController {

    @Reference
    OrderService orderService ;

    @Reference
    PaymentService paymentService;
    @RequestMapping("/toPay")
    public String toPay(HttpServletRequest request, Model model,String outTradeNo){
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

       OmsOrder omsOrder = orderService.selectOrderByOrSn(outTradeNo);
        model.addAttribute("nickName",nickname);
        model.addAttribute("totalAmount",omsOrder.getPayAmount());
        model.addAttribute("orderId",omsOrder.getId());
        model.addAttribute("outTradeNo",outTradeNo);
        return "index" ;
    }

    @RequestMapping("/alipay")
    public ModelAndView alipay(String orderId,String outTradeNo){
        //对接支付宝接口
        //通过支付宝的sdk发送请求，支付宝会返回一个支付表单
        //扫描支付后支付宝会回调配置好的支付接口
        //这里直接默认支付成功

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOutTradeNo(orderId);
        paymentInfo.setOutTradeNo(outTradeNo);
        paymentInfo.setPaymentStatus("未支付");
        //生成交易信息存入DB
        paymentService.addPaymentInfo(paymentInfo);

        Map<String, String> map = new HashMap<>();
        map.put("orderId",orderId);
        return new ModelAndView("alipayCallback",map);
    }

    @RequestMapping("/mx")
    public String mx(){
        //对接微信支付接口
        return null ;
    }

    @RequestMapping("/alipayCallback")
    public String alipayCallback(String orderId){
        //支付完成 -> 修改订单 —> 调用库存服务 —> 调用物流服务
        //修改支付状态
        paymentService.editPaymentInfo(orderId);

        return "finish";
    }
}
