package com.zxst.shoop.controller;

import com.zxst.shoop.config.AliPayTemplate;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.service.UserService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController{

    @Resource
    private AliPayTemplate aliPayTemplate;


    @Resource
    private UserService userService;
    //校验用户名
    @RequestMapping("/checkUserName")
    public JsonResult checkUserName(String uname) {
        boolean flag = userService.checkUserName(uname);
        if (flag) {
            return  new JsonResult(FAIL);
        }else{
            return  new JsonResult(OK);
        }
    }

    @RequestMapping("/checkoldVal")
    public JsonResult checkoldVal(String oldval,HttpSession session) {
        boolean flag = userService.checkoldVal(oldval,getUserId(session));
        if (flag) {
            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }



    //用户数据保存
    @RequestMapping("/register")
    public JsonResult register(User user) {
        boolean flag = userService.register(user);
        if (flag) {
            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }

    //登录方法
    @RequestMapping("/login")
    public JsonResult login(User user, HttpSession session) {
        System.out.println(aliPayTemplate);
        User dbUser  = userService.login(user);
        if (dbUser!=null) {
            session.setAttribute("uid", dbUser.getUid());
            session.setAttribute("username", dbUser.getUsername());
            return  new JsonResult(OK,dbUser.getUid());
        }else{
            return  new JsonResult(FAIL);
        }
    }

    @RequestMapping("/modifiedPass")
    public JsonResult modifiedPass(String ps,HttpSession session) {
         boolean flag = userService.modifiedPass(ps,getUserId(session));
        if (flag) {
            session.removeAttribute("uid");
            session.removeAttribute("username");
            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }

    @RequestMapping("/editUserInfo")
    public JsonResult editUserInfo(User user,HttpSession session) {
        user.setUid(getUserId(session));
        user.setModifiedUser(getUserName(session));
        user.setModifiedTime(new java.util.Date());
        boolean flag = userService.eeditUserInfo(user);
        if (flag) {
            session.removeAttribute("uid");
            session.removeAttribute("username");
            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }

    @RequestMapping("/showOneUser")
    public User showOneUser(HttpSession session) {
       return userService.showOneUser(getUserId(session));
    }


}
