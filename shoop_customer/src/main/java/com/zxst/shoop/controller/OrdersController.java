package com.zxst.shoop.controller;

import com.alibaba.fastjson2.JSON;
import com.zxst.shoop.entity.Address;
import com.zxst.shoop.entity.Order;
import com.zxst.shoop.service.AddressService;
import com.zxst.shoop.service.OrderService;
import com.zxst.shoop.util.JsonResult;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import org.apache.tomcat.Jar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/orders")
public class OrdersController  extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private AddressService addressService;

    @Resource
    private OrderService orderService;

    //预支付订单 redis
    @RequestMapping("/createOrder")
    public JsonResult createOrder(Double totalMoney, Integer sendAddress, String nids, HttpSession session) {
        System.out.println(totalMoney + "====" + sendAddress + "====" + nids);
        String substring = nids.substring(0, nids.lastIndexOf(','));

        Integer userId = getUserId(session);
        String userName = getUserName(session);

        String oid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Order order = new Order();
        order.setOid(oid);
        order.setUid(userId);
        order.setTotalPrice(totalMoney);
        order.setStatus(0); // 0 表示未支付
        order.setOrderTime(new java.util.Date());

        // 通过收货地址的 id 查询收获地址的信息
        Address address = addressService.getAddressByAid(sendAddress);
        order.setRecvName(address.getName());
        order.setRecvAddress(address.getAddress());
        order.setRecvProvince(address.getProvinceName());
        order.setRecvCity(address.getCityName());
        order.setRecvPhone(address.getPhone());
        order.setRecvArea(address.getAreaName());
        String jsonString = JSON.toJSONString(order);

        // 检查 Redis 中是否已存在该用户的订单
        Boolean b = redisTemplate.hasKey(userName);
        if (b) {
            return new JsonResult(FAIL, null, "您有未支付的订单");
        } else {
            // 1. 将订单信息保存到 MySQL 数据库
            int result = orderService.createOrder(order);

            if (result <= 0) {
                return new JsonResult(FAIL, null, "订单创建失败，请重试");
            }

            // 2. 将订单信息存入 Redis，设置 3 分钟过期时间
            ValueOperations<String, String> svo = redisTemplate.opsForValue();
            svo.set(userName, jsonString, 3, TimeUnit.MINUTES);

            // 3. 存放订单关联的商品信息到 Redis
            JsonResult jsonResult = new JsonResult(OK, "uids", substring);
            svo.set(userName + "nids", JSON.toJSONString(jsonResult), 3, TimeUnit.MINUTES);

            return new JsonResult(OK, null, oid);
        }
    }

    //展示所有用户订单
    @RequestMapping("/showOrdersInfo")
    public JsonResult showOrdersInfo(HttpSession session) {
        try {
            Integer userId = getUserId(session);
            if (userId == null) {
                logger.warn("User not logged in");
                return new JsonResult(401, "用户未登录");
            }
            logger.info("Fetching orders for userId: {}", userId);
            List<Order> orders = orderService.showOrderInfo(userId);
            logger.info("Found {} orders for userId: {}", orders != null ? orders.size() : 0, userId);
            return new JsonResult(OK, orders);
        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return new JsonResult(500, "获取订单数据失败: " + e.getMessage());
        }
    }

}
