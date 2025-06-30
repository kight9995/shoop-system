package com.zxst.shoop.config;

import com.zxst.shoop.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//将 LoginInterceptor 拦截器加载到配置类中
//boot整合spingmvc  提供了一个可以配置mvc组件工具  WebMvcConfigurer

@Configuration
public class LoginInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                // 配置拦截的路径
                .addPathPatterns("/**")
                // 配置放行的路径
                .excludePathPatterns(
                        "/",
                        "/index.html",
                        "/users/login",
                        "/users/logout",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/loginstyle/**",
                        "/favicon.ico"
                );
    }
}
