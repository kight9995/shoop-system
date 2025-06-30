package com.zxst.shoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxst.shoop.entity.Order;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.mapper.OrderItemMapper;
import com.zxst.shoop.mapper.OrderMapper;
import com.zxst.shoop.mapper.UserMapper;
import com.zxst.shoop.service.OrderService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Order> showOrderInfo(Integer userId) {
        return orderMapper.showOrderInfoByUid(userId);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Order> list = orderMapper.findPage(queryPageBean.getQueryString());
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Transactional
    @Override
    public boolean deleteOrderInfo(String oid) {
        boolean flag = false;
        //删除订单详情信息
        flag = orderItemMapper.deleteInfoByOid(oid)>0;
        //删除订单的信息
        flag = orderMapper.deleteInfoByOid(oid)>0;
        return flag;
    }

    @Override
    @Transactional
    public int createOrder(Order order) {
        try {
            // 调用 Mapper 插入订单数据
            return orderMapper.saveOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;  // 发生异常时返回0表示失败
        }
    }
    @Override
    public JsonResult showOrderDetail(String oid) {
        //展示订单  同时展示订单的用户
        Order order = orderMapper.getOrderByOid(oid);
        User userById = userMapper.findUserById(order.getUid());
        order.setUser(userById);
        return new JsonResult(200,order);
    }
}
