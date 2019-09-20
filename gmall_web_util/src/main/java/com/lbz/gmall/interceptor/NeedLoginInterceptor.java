package com.lbz.gmall.interceptor;

import com.alibaba.fastjson.JSON;
import com.lbz.gmall.annotation.LoginRequire;
import com.lbz.gmall.util.CookieUtil;
import com.lbz.gmall.util.HttpClientUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lbz
 * @create 2019-09-08 14:31
 */
@Component
public class NeedLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断方法是否使用LoginRequire注解
        HandlerMethod method = (HandlerMethod) handler;
        LoginRequire methodAnnotation = method.getMethodAnnotation(LoginRequire.class);
        if(methodAnnotation == null){
            //方法不需要验证登录
            return true ;
        }
        String token = "" ;

        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        String newToken = request.getParameter("newToken");
        if(!StringUtils.isEmpty(oldToken)){
            token = oldToken ;
        }
        if(!StringUtils.isEmpty(newToken)){
            token = newToken ;

        }
        //是否必须登录
        boolean mustLogin = methodAnnotation.MustLogin();
        Map<String,String> result = new HashMap<>();
        String status = "";
        if(!StringUtils.isEmpty(token)){
            //调用认证中心
            Map<String,String> map = new HashMap<>();
            map.put("token",token);
            String currentIP = request.getRemoteAddr();
            String jsonString = HttpClientUtil.doGet("http://localhost:8110/verify?currentIP="+currentIP, map);
            result = JSON.parseObject(jsonString,result.getClass());
            status = result.get("status");
        }
        if(mustLogin){
            //必须登录
            if(!StringUtils.isEmpty(status)&&status.equals("success")){
                //验证成功
                //重新刷新cookie的过期时间
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                //讲token携带的用户信息写入
                request.setAttribute("memberId",result.get("memberId"));
                request.setAttribute("nickname",result.get("nickname"));
            }else {
                //认证失败，需要登录或重新登录
                StringBuffer returnUrl = request.getRequestURL();
                response.sendRedirect("http://localhost:8110/toLogin?returnUrl="+returnUrl );
                return false ;
            }

        }else {
            //不登录也能使用

            //如果登录了就要登录验证
            if(!StringUtils.isEmpty(status)&&status.equals("success")) {
                //验证成功
                //重新刷新cookie的过期时间
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                //讲token携带的用户信息写入
                request.setAttribute("memberId", result.get("memberId"));
                request.setAttribute("nickname", result.get("nickname"));
            }
            return true ;
        }
        return true ;
    }
}
