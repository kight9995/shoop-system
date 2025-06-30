package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Cart;
import com.zxst.shoop.service.CartService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.vo.CartVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController{

    @Resource
    private CartService cartService;

    @RequestMapping("/addProToCart")
    public JsonResult addProToCart(Integer num, Integer pid, Integer price, HttpSession session) {
        // 检查用户是否登录
        Integer userId = getUserId(session);
        if (userId == null || userId == 0) {
            return new JsonResult(300, "请先登录");
        }

        String userName = getUserName(session);
        try {
            boolean flag = cartService.addProToCart(num, pid, price, userId, userName);
            if (flag) {
                return new JsonResult(OK);
            } else {
                return new JsonResult(FAIL, "添加购物车失败");
            }
        } catch (Exception e) {
            return new JsonResult(FAIL, "系统异常：" + e.getMessage());
        }
    }

    //查看购物车
    @RequestMapping("/showCarInfo")
    public JsonResult showCarInfo(HttpSession session){
        Integer userId = getUserId(session);
        List<CartVo> list = cartService.showCarInfo(userId);
        if (list.size()>0){
            return  new JsonResult(OK,list);
        }else {
            return  new JsonResult(FAIL);
        }
    }

    //点击 +
    @RequestMapping("/addNum")
    public JsonResult addNum(Integer cid){
       boolean flag =  cartService.addNum(cid);
        if (flag){
            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }

    @RequestMapping("/reduceNum")
    public JsonResult reduceNum(Integer cid){
        boolean flag =  cartService.reduceNum(cid);
        if (flag){
            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }

    @RequestMapping("/getTotalNum")
    public List<Cart> getTotalNum(){
       return  cartService.getTotalNum();
    }

    @RequestMapping("/batchDelete")
    public JsonResult batchDelete(Integer[] ids){
        boolean flag = cartService.batchDelete(ids);
        if (flag){

            return  new JsonResult(OK);
        }else{
            return  new JsonResult(FAIL);
        }
    }

    @RequestMapping("/getCarInfoByIds")
    public List<CartVo> getCarInfoByIds(String ids){
        System.out.println(ids+"=====");
        if (ids == null || "null".equalsIgnoreCase(ids) || ids.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return cartService.getCarInfoByIds(ids);
    }


}
