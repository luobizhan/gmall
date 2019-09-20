package com.lbz.gmall.config;

import com.lbz.gmall.interceptor.NeedLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lbz
 * @create 2019-09-08 14:28
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public NeedLoginInterceptor needLoginInterceptor(){
        return  new NeedLoginInterceptor();
    }

    /**
     * @param registry 配置静态资源放行
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(needLoginInterceptor()).excludePathPatterns("/static/**");
    }
}
