package com.zxst.shoop.controller;

import com.mysql.cj.exceptions.CJConnectionFeatureNotAvailableException;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.service.UserService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController  extends BaseController{

    @Resource
    private UserService userService;

    @RequestMapping("/findPage")
    @ResponseBody
    public PageResult findPage(@RequestBody  QueryPageBean queryPageBean) {
        return  userService.findPage(queryPageBean);
    }

    //改变用户的状态
    @RequestMapping("/updateUserStatus")
    @ResponseBody
    public JsonResult updateUserStatus(Integer uid) {
        //根据用户id获取用户信息
        User user = userService.showOneUser(uid);
        Integer isDelete = user.getIsDelete();
        if (isDelete == 1) {
            user.setIsDelete(0);
        }else{
            user.setIsDelete(1);
        }
        user.setModifiedTime(new java.util.Date());
        boolean flag = userService.eeditUserInfo(user);
        if (flag) {
            return new JsonResult(200);
        }else{
            return new JsonResult(300);
        }
    }


    @RequestMapping("/login")
    public String login(User user, HttpSession session, Model model) {
        // 如果用户已经登录，直接重定向到主页
        if (session.getAttribute("username") != null) {
            return "redirect:/pages/main.html";
        }
        User dbUser = userService.login(user);
        if (dbUser != null) {
            session.setAttribute("username",dbUser.getUsername());
            session.setAttribute("uid",dbUser.getUid());
            return "redirect:/pages/main.html";
        }else{
            return "redirect:/index.html";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("uid");
        session.invalidate();
        //回到登录页面
        return "redirect:/index.html";
    }

    @RequestMapping("/")
    public String index(HttpSession session) {
        // 如果用户已登录，重定向到主页
        if (session.getAttribute("username") != null) {
            return "redirect:/pages/main.html";
        }
        // 否则重定向到登录页
        return "redirect:/index.html";
    }
}
