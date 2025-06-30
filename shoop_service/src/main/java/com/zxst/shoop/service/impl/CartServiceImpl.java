package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Cart;
import com.zxst.shoop.mapper.CartMapper;
import com.zxst.shoop.service.CartService;
import com.zxst.shoop.vo.CartVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Override
    public boolean addProToCart(Integer num, Integer pid, Integer price, Integer userId,String userName) {
        boolean flag = false;
        //判断当前用户 是否已经添加了该商品
        Cart cart =  cartMapper.getCartByPidAndUid(pid,userId);
        if (cart == null) {
            cart = new Cart();
            cart.setNum(num);
            cart.setPrice(price);
            cart.setPid(pid);
            cart.setUid(userId);
            cart.setCreatedUser(userName);
            cart.setModifiedUser(userName);
            cart.setCreatedTime(new java.util.Date());
            cart.setModifiedTime(new java.util.Date());
            flag = cartMapper.saveCart(cart)>0;
        }else{
            Integer num1 = cart.getNum();
            Integer num2 = num1 + num;
            cart.setNum(num2);
            flag = cartMapper.updateProNum(cart)>0;
        }
        return flag;
    }

    @Override
    public List<CartVo> showCarInfo(Integer userId) {
        return cartMapper.showCarInfo(userId);
    }

    @Override
    public boolean addNum(Integer cid) {
        Cart cart = cartMapper.getCartByPk(cid);
        cart.setNum(cart.getNum()+1);
        cart.setModifiedTime(new java.util.Date());
        return  cartMapper.updateProNum(cart)>0;
    }

    @Override
    public boolean reduceNum(Integer cid) {
        Cart cart = cartMapper.getCartByPk(cid);
        cart.setNum(cart.getNum()-1);
        cart.setModifiedTime(new java.util.Date());
        return  cartMapper.updateProNum(cart)>0;
    }

    @Override
    public List<Cart> getTotalNum() {
        return cartMapper.getTotalNum();
    }

    @Override
    public boolean batchDelete(Integer[] ids) {
        return cartMapper.batchDelete(ids)>0;
    }

    @Override
    public List<CartVo> getCarInfoByIds(String ids) {
        if (ids == null || "null".equalsIgnoreCase(ids) || ids.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            String[] split = ids.split(",");
            Integer[] nnids = new Integer[split.length];
            int index = 0;
            for (String s : split) {
                if (s != null && !s.trim().isEmpty()) {
                    nnids[index] = Integer.parseInt(s.trim());
                    index++;
                }
            }
            if (index == 0) {
                return Collections.emptyList();
            }
            return cartMapper.getCarInfoByIds(nnids);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
}
