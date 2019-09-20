package com.lbz.gmall.service;

import com.lbz.gmall.bean.OmsCartItem;

import java.util.List;


/**
 * @author lbz
 * @create 2019-09-05 21:40
 */
public interface CartService {

    /**
     * 保存购物车清单
     * @param omsCartItems
     */
    void saveOmsCartItems(OmsCartItem omsCartItems);

    /**
     * 根据用户Id和商品Id查看是否存在记录
     * @param memberId
     * @param skuId
     * @return
     */
    boolean ifExSku(String memberId, String skuId);

    /**
     * 更新omsCartItem表
     * @param omsCartItem
     */
    void updateOmsCartItem(OmsCartItem omsCartItem);

    /**
     * 更新缓存
     * @param memberId
     */
    void flushCache(String memberId);

    /**
     * 从缓存中拿数据
     * @param memberId
     * @return
     */
    List<OmsCartItem> getCache(String memberId);

    /**
     * 修改isChecked字段的属性值
     * @param omsCartItem
     */
    void updateIsChecked(OmsCartItem omsCartItem);

    /**
     * 根据memberId从DB中获取购物车信息
     * @param memberId
     * @return
     */
    List<OmsCartItem> getByMemberId(String memberId);

    /**
     * 删除结算时选中的商品
     * @param omsCartItem
     */
    void deleteByChecked(OmsCartItem omsCartItem);
}
