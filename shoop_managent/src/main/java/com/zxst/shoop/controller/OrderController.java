package com.zxst.shoop.controller;


import com.zxst.shoop.service.OrderService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
       return  orderService.findPage(queryPageBean);
    }

    @RequestMapping("/deleteOrderInfo")
    public JsonResult deleteOrderInfo(String oid) {
        boolean flag = orderService.deleteOrderInfo(oid);
        if (flag) {
            return new JsonResult(200);
        }else{
            return new JsonResult(300);
        }
    }
    @RequestMapping("/showOrderDetail")
    public JsonResult showOrderDetail(String oid) {
        return  orderService.showOrderDetail(oid);
    }
}
