package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Address;
import com.zxst.shoop.service.AddressService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController extends BaseController{
    @Resource
    private AddressService addressService;

    //展示地址信息
    @RequestMapping("/showAddress")
    public List<Address> showAddress(HttpSession session){
        Integer userId = getUserId(session);
        return  addressService.showAddress(userId);
    }
    @RequestMapping("/setDefaultAddr")
    public JsonResult setDefaultAddr(Integer aid,HttpSession session){
        boolean flag = addressService.setDefaultAddr(aid,getUserId(session));
        if(flag){
            return new JsonResult(OK);
        }else{
            return new JsonResult(FAIL);
        }
    }

    //保存地址信息
    @RequestMapping("/saveAddress")
    public JsonResult saveAddress(Address address,HttpSession session){
        Integer userId = getUserId(session);
        String userName = getUserName(session);
        boolean flag = addressService.saveAddress(userId,userName,address);
        if(flag){
            return new JsonResult(OK);
        }else{
            return new JsonResult(FAIL);
        }
    }
}
