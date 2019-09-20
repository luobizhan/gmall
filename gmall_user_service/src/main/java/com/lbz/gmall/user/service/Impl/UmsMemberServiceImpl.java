package com.lbz.gmall.user.service.Impl;

import com.alibaba.fastjson.JSON;
import com.lbz.gmall.bean.UmsMember;
import com.lbz.gmall.bean.UmsMemberReceiveAddress;
import com.lbz.gmall.service.UmsMemberService;
import com.lbz.gmall.user.mapper.UmsMemberMapper;
import com.lbz.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.lbz.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-13 12:31
 */

@org.apache.dubbo.config.annotation.Service
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    
    @Autowired
    private UmsMemberMapper umsMemberMapper ;
    @Autowired
    private RedisUtil redisUtil ;
    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper ;
    
    @Override
    public List<UmsMember> selectAll() {
        List<UmsMember> umsMembers = umsMemberMapper.selectAll();
        return umsMembers;
    }

    @Override
    public String selectFromCache(UmsMember umsMember) {
        UmsMember umsMemberFromDB = (UmsMember) redisUtil.get("user:" + umsMember.getPassword() + ":Info");
        String umsString = JSON.toJSONString(umsMemberFromDB);
        return umsString;
    }

    @Override
    public UmsMember selectOne(UmsMember umsMember) {
        UmsMember umsMemberFromDb = umsMemberMapper.selectOne(umsMember);
        return umsMemberFromDb;
    }

    @Override
    public void putCache(UmsMember umsMember) {
        redisUtil.set("user:"+umsMember.getPassword()+":Info",umsMember) ;
    }

    @Override
    public UmsMember checkOAuthUser(String uid) {
        UmsMember umsMember = new UmsMember();
        umsMember.setUid(uid);
        UmsMember umsMemberFromDB = umsMemberMapper.selectOne(umsMember);
        return umsMemberFromDB;
    }

    @Override
    public void addOAuthUser(UmsMember umsMember) {
        umsMemberMapper.insertSelective(umsMember);
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddress(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> receiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return receiveAddresses;
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressByAddressId(String deliveryAddress) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = umsMemberReceiveAddressMapper.selectByPrimaryKey(deliveryAddress);
        return umsMemberReceiveAddress;
    }
}
