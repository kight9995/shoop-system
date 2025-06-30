package com.zxst.shoop.config;

import com.zxst.shoop.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//将 LoginInterceptor 拦截器加载到配置类中
//boot整合spingmvc  提供了一个可以配置mvc组件工具  WebMvcConfigurer

//@Configuration
public class LoginInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将我们自己的拦截器注册到springboot中
        registry.addInterceptor(new LoginInterceptor())
                //配置拦截的路径
                .addPathPatterns("/**")
                //配置放行的路径
                .excludePathPatterns("/","/web/login.html","/web/register.html","/index.html"
                ,"/bootstrap3/**","/css/**","/js/**","/images/**"
                ,"/users/login","/users/checkUserName","/users/register","/product/**","/category/**","/banner/**");
    }
}
