package com.zxst.shoop.controller;

import com.zxst.shoop.service.ex.*;
import com.zxst.shoop.util.JsonResult;
import org.junit.jupiter.api.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {

    public static final int OK = 200;
    public static final int FAIL = 300;
    /**
     * ExceptionHandler注解的作用是统一异常处理，只要是ServiceException类型的异常全部到这来处理
     * 自动将异常对象传递到此方法的参数列表上
     * 项目中产生了异常，被统一拦截到这，这个方法此时就充当的是请求处理方法，方法的返回值直接给到前端
     */
    @ExceptionHandler({ServiceException.class})
    public JsonResult handleException(Throwable e) {
        JsonResult jsonResult = new JsonResult(e);
        if (e instanceof UsernameDuplicatedException) {
            jsonResult.setCode(4000);
            jsonResult.setMsg("用户名被注册");
        } else if (e instanceof AddressCountLimitException) {
            jsonResult.setCode(4001);
            jsonResult.setMsg("用户的收货地址超出上限");
        } else if (e instanceof AddressNotFoundException) {
            jsonResult.setCode(4002);
            jsonResult.setMsg("用户的收货地址数据不存在");
        } else if (e instanceof AccessDeniedException) {
            jsonResult.setCode(4003);
            jsonResult.setMsg("收货地址数据非法访问异常");
        } else if (e instanceof UserNotFoundException) {
            jsonResult.setCode(4004);
            jsonResult.setMsg("用户名没有找到");
        } else if (e instanceof ProductNotFoundException) {
            jsonResult.setCode(4005);
            jsonResult.setMsg("产品未找到");
        } else if (e instanceof CartNotFoundException) {
            jsonResult.setCode(4006);
            jsonResult.setMsg("购物车信息未找到");
        } else if (e instanceof SaveInfoException) {
            jsonResult.setCode(5000);
            jsonResult.setMsg("添加信息时出现异常");
        } else if (e instanceof DeleteInfoException) {
            jsonResult.setCode(5001);
            jsonResult.setMsg("删除数据时发生未知异常");
        } else if (e instanceof PasswordNotMatchException) {
            jsonResult.setCode(5002);
            jsonResult.setMsg("密码错误");
        } else if (e instanceof UpdateException) {
            jsonResult.setCode(5003);
            jsonResult.setMsg("更新数据时产生未知异常");
        } else if (e instanceof OrderNotFoundException) {
            jsonResult.setCode(5004);
            jsonResult.setMsg("订单信息未找到");
        }
        return jsonResult;
    }


    /*
    *  从session中获取登录成功后放入到session中的id
    * */
    protected final Integer  getUserId(HttpSession session){
        Integer uidStr = (Integer)session.getAttribute("uid");
        if (uidStr != null) {
            return uidStr;
        }else{
            return 0;
        }
    }
    /*
    *  从session中获取登录成功后的放入到session中的用户名
    * */
    protected final String getUserName(HttpSession session){
        String uidStr = (String)session.getAttribute("username");
        if (uidStr != null) {
            return uidStr;
        }else{
            return  "";
        }
    }
}
