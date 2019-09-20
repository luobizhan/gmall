package com.lbz.gmall.cart.controller;

import com.alibaba.fastjson.JSON;
import com.lbz.gmall.annotation.LoginRequire;
import com.lbz.gmall.bean.OmsCartItem;
import com.lbz.gmall.bean.PmsSkuInfo;
import com.lbz.gmall.service.CartService;
import com.lbz.gmall.service.SkuService;
import com.lbz.gmall.util.CookieUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lbz
 * @create 2019-09-05 17:20
 */
@Controller
public class cartController {

    @Reference
    private SkuService skuService;
    @Reference
    private CartService cartService;

    private static final String cookieName = "cartListCookie";


    @LoginRequire(MustLogin = false)
    @RequestMapping("/checkCart")
    public String checkCartList(OmsCartItem omsCartItem, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {

        String memberId = (String) request.getAttribute("memberId");
        List<OmsCartItem> cartList = new ArrayList<>();
        if (memberId!=null) {
            //用户已登录，修改DB中isChecked的属性
            omsCartItem.setMemberId(memberId);
            cartService.updateIsChecked(omsCartItem);
            //从缓存中获取数据
            cartList = cartService.getCache(memberId);
            //计算每种商品的总价
            for (OmsCartItem cartItem : cartList) {
                cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            }

        } else {
            //用户未登陆，修改cookie中的isChecked值
            String cookieValue = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (!StringUtil.isBlank(cookieValue)) {
                cartList = JSON.parseArray(cookieValue, OmsCartItem.class);
                for (OmsCartItem cookieCart : cartList) {
                    //修改isChecked的值
                    if (cookieCart.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                        cookieCart.setIsChecked(omsCartItem.getIsChecked());
                    }
                    //计算每种商品的总价
                    cookieCart.setTotalPrice(cookieCart.getPrice().multiply(cookieCart.getQuantity()));
                }
            }
            //更新cookie
            CookieUtil.setCookie(request, response, cookieName, JSON.toJSONString(cartList), 60 * 60 * 72, true);
        }
        model.addAttribute("allPrice", getAllPrice(cartList));
        model.addAttribute("cartList", cartList);

        return "cartListInner";
    }

    @LoginRequire(MustLogin = false)
    @RequestMapping("/cartList")
    public String cartList(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        model.addAttribute("userId",memberId);
        model.addAttribute("nickname",nickname);

        if (StringUtil.isBlank(memberId)) {
            //未登录
            //拿到cookie中的数据，判断是否有值
            String cookieValue = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (!StringUtil.isBlank(cookieValue)) {
                List<OmsCartItem> omsCartItems = JSON.parseArray(cookieValue, OmsCartItem.class);
                for (OmsCartItem omsCartItem : omsCartItems) {
                    //计算每种商品的总价
                    omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
                }

                model.addAttribute("allPrice", getAllPrice(omsCartItems));
                model.addAttribute("cartList", omsCartItems);
            }
        } else {
            //已登录
            //同步cookie中的购物车
            //获取缓存中的数据，回复前端请求
            List<OmsCartItem> cache = cartService.getCache(memberId);
            if(cache==null||cache.size()==0){
                //更新缓存
                cartService.flushCache(memberId);
            }
            //计算每种商品的总价
            for (OmsCartItem omsCartItem : cache) {
                omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
            }
            model.addAttribute("allPrice", getAllPrice(cache));
            model.addAttribute("cartList", cache);
        }
        return "cartList";
    }

    private Object getAllPrice(List<OmsCartItem> omsCartItems) {

        BigDecimal bigDecimal = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            //判断是否选中
            if (omsCartItem.getIsChecked() != null && omsCartItem.getIsChecked().equals("1")) {
                //计算价格
                bigDecimal = bigDecimal.add(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));

            }
        }
        return bigDecimal;
    }

    @LoginRequire(MustLogin = false)
    @RequestMapping("/addToCart")
    public String toCartList(String skuId, int num, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        //获取要加入购物车的skuInfo
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        //添加的商品信息返回
        model.addAttribute("skuInfo", pmsSkuInfo);

        //封装OmsCartItem属性
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(num));
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        //默认isChecked = 1
        omsCartItem.setIsChecked("1");
        //封装到集合中
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        String memberId = (String) request.getAttribute("memberId");


        omsCartItem.setMemberId(memberId);
        //判断用户是否登录
        if (StringUtil.isBlank(memberId)) {
            //用户没有登录,
            //判断客户端是否存在cookie
            String cookieValue = CookieUtil.getCookieValue(request, cookieName, true);
            if(StringUtil.isBlank(cookieValue)){
                //cookie为空
                omsCartItems.add(omsCartItem);
            }else {
                //cookie不为空，把cookie的值加入到omsCartItems中
                 omsCartItems = JSON.parseArray(cookieValue,OmsCartItem.class);
                //判断是否有相同的sku
                boolean exit = exitOrNot(cookieValue, omsCartItem);
                if (!exit) {
                    //不存在相同的sku,将商品信息先存入cookie中
                    omsCartItems.add(omsCartItem);
                } else {
                    //cookie中存在此sku
                    omsCartItems = JSON.parseArray(cookieValue, OmsCartItem.class);
                    for (OmsCartItem cartItems : omsCartItems) {
                        if (cartItems.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            //如果存在相同的sku
                            cartItems.setQuantity(omsCartItem.getQuantity().add(cartItems.getQuantity()));
                            cartItems.setModifyDate(new Date());
                        }
                    }
                }
            }

            //更新cookie
            CookieUtil.setCookie(request, response, cookieName, JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            //用户已经登录，判断数据库中是否存在此商品
            boolean flag = cartService.ifExSku(memberId, skuId);
            if (flag) {
                //DB中存在此商品，更新omsCartItem表
                cartService.updateOmsCartItem(omsCartItem);

            } else {
                //DB中不存在此商品，新增操作
                cartService.saveOmsCartItems(omsCartItem);
            }
            //更新缓存
            cartService.flushCache(memberId);
        }
        model.addAttribute("skuNum", omsCartItem.getQuantity());

        return "success";
    }

    private boolean exitOrNot(String cookieValue, OmsCartItem oms) {
        if (cookieValue != null) {
            List<OmsCartItem> omsCartItems = JSON.parseArray(cookieValue, OmsCartItem.class);
            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getProductSkuId().equals(oms.getProductSkuId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
