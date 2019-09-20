package com.lbz.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lbz.gmall.bean.UmsMember;
import com.lbz.gmall.service.UmsMemberService;
import com.lbz.gmall.util.HttpClientUtil;
import com.lbz.gmall.util.JwtUtil;
import io.searchbox.strings.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lbz
 * @create 2019-09-10 13:58
 */

@Controller
public class PassportController {

    @Reference
    private UmsMemberService umsMemberService ;

    @RequestMapping("vlogin")
    public String vlogin(String code,HttpServletRequest request){

        //使用code交换access_token
        Map<String,String> map = new HashMap<>();
        map.put("client_id","3913909047");
        map.put("client_secret","7556750c04005563d442d44bcd9c54d9");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://192.168.1.6:8110/vlogin");
        map.put("code",code);
        String result = HttpClientUtil.doPost("https://api.weibo.com/oauth2/access_token", map);
        Map resultMap = JSON.parseObject(result, map.getClass());
        String accessToken = (String) resultMap.get("access_token");
        String uid = (String) resultMap.get("uid");

        Map<String,String> parma = new HashMap();
        parma.put("uid",uid);
        parma.put("access_token",accessToken);
        //使用access_token获取用户信息
        String oauthUser = HttpClientUtil.doGet("https://api.weibo.com/2/users/show.json",parma);
        Map oauthUserMap = JSON.parseObject(oauthUser,Map.class);

        String gender = (String) oauthUserMap.get("gender");
        UmsMember umsMember = new UmsMember();
        if(gender.equals("f")){
            gender = "1";
        }else {
            gender = "0";
        }
        umsMember.setGender(gender);
        umsMember.setSourceType("2");
        umsMember.setUid(uid);
        umsMember.setAccessCode(code);
        umsMember.setAccessToken(accessToken);
        umsMember.setNickname((String) oauthUserMap.get("name"));

        //根据uid查看数据库中是否存在此用户
        uid = umsMember.getUid();
        if(uid!=null){
            UmsMember umsMemberCheck = umsMemberService.checkOAuthUser(uid);
            if(umsMemberCheck==null){
                umsMemberService.addOAuthUser(umsMember);
            }
        }

        //生成token
        //获取用户ip
        String ip = "";
        ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)) {
            //直接获取ip
            ip = "0:0:0:0:0:0:0:1";
        }
        if (StringUtils.isBlank(ip)) {
            //设置一个虚拟ip
            ip = "127.0.0.1";
        }

        //使用jwt，颁发token
        String name = (String) oauthUserMap.get("name");
        Map<String, String> map2 = new HashMap<>();
        map2.put("memberId", uid);
        map2.put("nickname", name);

       String token = JwtUtil.encode("gmall",map2,ip);


        return "redirect:http://localhost:8090/index?newToken="+token;
    }

    @ResponseBody
    @RequestMapping("/verify")
    public String verify(String token, String currentIP){

        Map<String,String> result = new HashMap<>();
        if(StringUtil.isBlank(token)){
            result.put("status","error");
        }else {
            Map decode = JwtUtil.decode("gmall", token, currentIP);
            if(decode!=null){
                result.put("memberId", (String) decode.get("memberId"));
                result.put("nickname", (String) decode.get("nickname"));
                result.put("status","success");
            }else {
                result.put("status","error");
            }
        }
        return  JSON.toJSONString(result);
    }

    @RequestMapping("/toLogin")
    public String toLogin(String returnUrl,Model model){

        model.addAttribute("returnUrl",returnUrl);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/login")
    public String login(Model model, UmsMember umsMember, HttpServletRequest request){
        //查询缓存
        String token ="";
        UmsMember umsMemberInfo = new UmsMember();
        if(umsMember!=null) {
            String result = umsMemberService.selectFromCache(umsMember);
            UmsMember resultUmsMember = JSON.parseObject(result, UmsMember.class);

            //获取用户ip
            String ip = "";
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isBlank(ip)) {
                //直接获取ip
                ip = request.getRemoteAddr();
            }
            if (StringUtils.isBlank(ip)) {
                //设置一个虚拟ip
                ip = "127.0.0.1";
            }

            if (resultUmsMember!=null) {
                //缓存中存在用户
                umsMemberInfo = JSON.parseObject(result, UmsMember.class);

                //使用jwt，颁发token
                Map<String, String> map = new HashMap<>();
                map.put("memberId", umsMemberInfo.getId());
                map.put("nickName", umsMemberInfo.getNickname());

                token = JwtUtil.encode("gmall",map,ip);

            }else {
                //缓存中不存在用户,从数据库中查找
                umsMemberInfo = umsMemberService.selectOne(umsMember);
                if(umsMemberInfo!=null){
                    //存在用户，放入缓存中
                    umsMemberService.putCache(umsMemberInfo);

                    //使用jwt，颁发token
                    Map<String, String> map = new HashMap<>();
                    map.put("memberId", umsMemberInfo.getId());
                    map.put("nickname", umsMemberInfo.getNickname());

                    token = JwtUtil.encode("gmall",map,ip);
                }else {
                    //账号和密码不匹配
                    token = "username or password err";
                }
            }
        }

        return token;
    }
}
