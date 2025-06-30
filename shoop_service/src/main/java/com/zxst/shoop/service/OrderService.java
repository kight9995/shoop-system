package com.zxst.shoop.service;

import com.zxst.shoop.entity.Order;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;

import java.util.List;

public interface OrderService {
    List<Order> showOrderInfo(Integer userId);

    PageResult findPage(QueryPageBean queryPageBean);

    boolean deleteOrderInfo(String oid);

    int createOrder(Order order);

    JsonResult showOrderDetail(String oid);
}
