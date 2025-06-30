package com.zxst.shoop.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的拦截器  对请求进行拦截
 * 判断session中是否有数据 有数据放行  没有数据拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // 静态资源直接放行
        if (isStaticResource(requestURI)) {
            return true;
        }
        
        // 检查用户是否已登录
        Object uid = request.getSession().getAttribute("uid");
        if (uid == null) {
            // 未登录且不是登录页面，重定向到登录页
            if (!"/index.html".equals(requestURI) && !"/".equals(requestURI)) {
                response.sendRedirect("/index.html");
                return false;
            }
        } else {
            // 已登录但访问的是登录页，重定向到首页
            if ("/index.html".equals(requestURI) || "/".equals(requestURI)) {
                response.sendRedirect("/pages/main.html");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 判断是否为静态资源请求
     */
    private boolean isStaticResource(String uri) {
        return uri.startsWith("/css/") || 
               uri.startsWith("/js/") || 
               uri.startsWith("/images/") || 
               uri.startsWith("/loginstyle/");
    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
