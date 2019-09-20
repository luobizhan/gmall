package com.lbz.gmall.user.controller;

import com.lbz.gmall.bean.UmsMember;
import com.lbz.gmall.service.UmsMemberService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 11:34
 */
@Controller
public class UserController {

    @Reference
    private UmsMemberService umsMemberService ;

    @ResponseBody
    @RequestMapping("/list")
    public List<UmsMember> list(){
        return umsMemberService.selectAll() ;
    }
}
