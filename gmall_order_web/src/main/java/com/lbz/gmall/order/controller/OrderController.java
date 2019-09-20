package com.lbz.gmall.order.controller;

import com.lbz.gmall.annotation.LoginRequire;
import com.lbz.gmall.bean.OmsCartItem;
import com.lbz.gmall.bean.OmsOrder;
import com.lbz.gmall.bean.OmsOrderItem;
import com.lbz.gmall.bean.UmsMemberReceiveAddress;
import com.lbz.gmall.service.CartService;
import com.lbz.gmall.service.OrderService;
import com.lbz.gmall.service.UmsMemberService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author lbz
 * @create 2019-09-17 10:52
 */

@Controller
public class OrderController {

    @Reference
    private CartService cartService;
    @Reference
    private UmsMemberService umsMemberService;
    @Reference
    private OrderService orderService;

    @LoginRequire(MustLogin = true)
    @RequestMapping("/submitOrder")
    public String submitOrder(String deliveryAddress, String tradeCode, HttpServletRequest request, Model model) {

        //获取当前用户id和nickname ;
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        List<OmsOrderItem> items = new ArrayList<>();
        OmsOrder omsOrder = new OmsOrder();
        //检验交易码
        boolean flag = orderService.checkCode(tradeCode, memberId);
        //提交订单业务
        if (flag) {
            //获取提交购物车中的数据
            List<OmsCartItem> omsCartItemList = cartService.getByMemberId(memberId);
            //生成订单信息
            for (OmsCartItem omsCartItem : omsCartItemList) {
                //被选中去结算
                if (omsCartItem.getIsChecked().equals("1")) {
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    //验价,验库存
                    String skuId = omsCartItem.getProductSkuId();
                    BigDecimal price = omsCartItem.getPrice();
                    boolean checkPrice = orderService.checkPrice(price,skuId);
                    if (checkPrice) {
                        //封装orderItem
                        omsOrderItem.setProductName(omsCartItem.getProductName());
                        omsOrderItem.setProductId(omsCartItem.getProductId());
                        omsOrderItem.setProductPic(omsCartItem.getProductPic());
                        omsOrderItem.setProductPrice(omsCartItem.getPrice());
                        omsOrderItem.setProductSkuCode(omsCartItem.getProductSkuCode());
                        omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                        omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());

                        String outTradeNo = "gmall";
                        outTradeNo = outTradeNo + System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
                        outTradeNo = outTradeNo + sdf.format(new Date());

                        omsOrderItem.setOrderSn(outTradeNo);
                        items.add(omsOrderItem);
                    } else {
                        return "tradeFail";
                    }
                }
            }
            //封装order信息
            omsOrder.setOmsOrderItems(items);
            omsOrder.setCreateTime(new Date());
            omsOrder.setDiscountAmount(null);
            //omsOrder.setFreightAmount(); 运费，支付后产生
            omsOrder.setMemberId(memberId);
            omsOrder.setMemberUsername(nickname);
            omsOrder.setNote("马上发货。");
            String outTradeNo = "gmall";
            outTradeNo = outTradeNo + System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo = outTradeNo + sdf.format(new Date());

            omsOrder.setOrderSn(outTradeNo);//外部订单号
            omsOrder.setPayAmount((BigDecimal) getAllPrice(items));
            omsOrder.setOrderType(1);
            //查找收货人地址信息后封装
            UmsMemberReceiveAddress umsMemberReceiveAddress = umsMemberService.getReceiveAddressByAddressId(deliveryAddress);
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());

            //新增订单,删除购物车中被选中的商品
            orderService.addOrder(omsOrder);
            //同步购物车缓存
            cartService.flushCache(memberId);

            //重定向到支付服务
            return "redirect:http://localhost:8210/toPay?outTradeNo="+outTradeNo;
        }else {
            return "tradeFail";
        }

    }

    @LoginRequire(MustLogin = true)
    @RequestMapping("/toTrade")
    public String toTrade(HttpServletRequest request, Model model) {
        String nickname = (String) request.getAttribute("nickname");
        String memberId = (String) request.getAttribute("memberId");
        model.addAttribute("nickName", nickname);

        //获取用户收货地址信息
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = umsMemberService.getReceiveAddress(memberId);
        model.addAttribute("userAddressList", umsMemberReceiveAddressList);
        //从缓存中拿到选中的购物车商品
        List<OmsCartItem> omsCartItemFormCache = cartService.getCache(memberId);
        //给订单列表准备的容器
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        //组装OmsOrderItem
        if (omsCartItemFormCache != null && omsCartItemFormCache.size() > 0) {
            for (OmsCartItem omsCartItem : omsCartItemFormCache) {
                if (omsCartItem.getIsChecked().equals("1")) {
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductName(omsCartItem.getProductName());
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setProductSkuCode(omsCartItem.getProductSkuCode());
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItems.add(omsOrderItem);
                }
            }
        }
        model.addAttribute("totalAmount", getAllPrice(omsOrderItems));
        model.addAttribute("orderDetailList", omsOrderItems);
        //生成交易码
        String code = UUID.randomUUID().toString();
        //保存到cache中
        orderService.saveCode(code, memberId);
        model.addAttribute("tradeCode", code);

        return "trade";
    }

    private Object getAllPrice(List<OmsOrderItem> omsOrderItems) {

        BigDecimal bigDecimal = new BigDecimal("0");
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            //计算价格
            bigDecimal = bigDecimal.add(omsOrderItem.getProductPrice().multiply(omsOrderItem.getProductQuantity()));
        }
        return bigDecimal;
    }
}
