package com.lbz.gmall.service;

import com.lbz.gmall.bean.UmsMember;
import com.lbz.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-13 12:25
 */
public interface UmsMemberService {

    List<UmsMember> selectAll();

    /**
     * 从Cache中查找用户信息
     * @param umsMember
     * @return
     */
    String selectFromCache(UmsMember umsMember);

    /**
     * 从DB中查找用户信息
     * @param umsMember
     * @return
     */
    UmsMember selectOne(UmsMember umsMember);

    /**
     * 将用户信息存入缓存
     * @param umsMember
     */
    void putCache(UmsMember umsMember);

    /**
     * 根据Uid检查是否存在用户
     * @param uid
     * @return
     */
    UmsMember checkOAuthUser(String uid);

    /**
     * 新增OAuthUser
     * @param umsMember
     */
    void addOAuthUser(UmsMember umsMember);

    /**
     * 根据用户Id返回用户收货地址
     * @param memberId
     * @return
     */
    List<UmsMemberReceiveAddress> getReceiveAddress(String memberId);

    /**
     * 根据addressId 查找地址信息
     * @param deliveryAddress
     * @return
     */
    UmsMemberReceiveAddress getReceiveAddressByAddressId(String deliveryAddress);
}
