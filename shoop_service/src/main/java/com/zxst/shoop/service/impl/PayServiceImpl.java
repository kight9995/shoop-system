package com.zxst.shoop.service.impl;

import com.alibaba.fastjson2.JSON;
import com.zxst.shoop.entity.Cart;
import com.zxst.shoop.entity.Order;
import com.zxst.shoop.entity.OrderItem;
import com.zxst.shoop.entity.Product;
import com.zxst.shoop.mapper.CartMapper;
import com.zxst.shoop.mapper.OrderItemMapper;
import com.zxst.shoop.mapper.OrderMapper;
import com.zxst.shoop.mapper.ProductMapper;
import com.zxst.shoop.service.PayService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class PayServiceImpl implements PayService {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CartMapper cartMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductMapper productMapper;

    @Transactional
    public boolean saveOrder(String userName) {
        boolean flag = false;
        //从redis中获取预支付信息
        ValueOperations<String, String> sso = redisTemplate.opsForValue();

        System.out.println(userName);
        //从redis中提取预支付订单信息
        String orderJson = sso.get(userName);
        //订单数据
        Order order = JSON.parseObject(orderJson, Order.class);
        order.setStatus(1);
        order.setPayTime(new java.util.Date());
        order.setCreatedUser(userName);
        order.setModifiedUser(userName);
        order.setCreatedTime(new java.util.Date());
        order.setModifiedTime(new java.util.Date());
        //在订单表中持久化数据
        flag = orderMapper.saveOrder(order)>0;

        //订单中包含的商品信息(订单详情表)
        String ids = sso.get(userName+"nids");//"48,59"
        JsonResult jsonResult = JSON.parseObject(ids, JsonResult.class);
        System.out.println(jsonResult.getData().toString()+"============");
        String redisIds = (String)jsonResult.getData();
        String[] split = redisIds.split(",");

        int mark = 0;
        Integer [] nids = new Integer[split.length];
        for (String id : split) {
            System.out.println(id+"=================");
            //购物车 数据的批量出的id集合
            int cid = Integer.parseInt(id);
            nids[mark] = cid;
            mark++;

            //添加订单详情
            Cart cartByPk = cartMapper.getCartByPk(cid);
            //根据车的商品id查询商品
            Product infoById = productMapper.getInfoById(cartByPk.getPid());
            OrderItem orderItem = new OrderItem();
            orderItem.setOid(order.getOid());
            orderItem.setPid(cartByPk.getPid());
            orderItem.setNum(cartByPk.getNum());
            orderItem.setTitle(infoById.getTitle());
            orderItem.setPrice(infoById.getPrice()*cartByPk.getNum()); //商品的小计
            orderItem.setImage(infoById.getImage());
            orderItem.setCreatedUser(userName);
            orderItem.setModifiedUser(userName);
            orderItem.setCreatedTime(new java.util.Date());
            orderItem.setModifiedTime(new java.util.Date());
            flag = orderItemMapper.saveOrderItem(orderItem)>0;

            //改变商品数量
            infoById.setNum(infoById.getNum()-cartByPk.getNum());
            flag = productMapper.updateInfo(infoById)>0;

        }
        //删除购物车中某买的商品信息
        flag = cartMapper.batchDelete(nids)>0;

        //删除redias中的待支付记录
        redisTemplate.delete(userName);
        redisTemplate.delete(userName+"nids");

        return  true;
    }
}
