package com.zxst.shoop.service;

import com.zxst.shoop.entity.Cart;
import com.zxst.shoop.vo.CartVo;

import java.util.List;

public interface CartService {
    boolean addProToCart(Integer num, Integer pid, Integer price, Integer userId,String userName);

    List<CartVo> showCarInfo(Integer userId);

    boolean addNum(Integer cid);

    boolean reduceNum(Integer cid);

    List<Cart> getTotalNum();

    boolean batchDelete(Integer[] ids);

    List<CartVo> getCarInfoByIds(String ids);
}
