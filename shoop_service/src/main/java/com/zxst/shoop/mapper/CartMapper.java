package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Cart;
import com.zxst.shoop.vo.CartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    Cart getCartByPidAndUid(@Param("pid") Integer pid, @Param("uid") Integer userId);

    int saveCart(Cart cart);

    int  updateProNum(Cart cart);

    List<CartVo> showCarInfo(@Param("uid") Integer userId);

    Cart getCartByPk(@Param("cid") Integer cid);

    List<Cart> getTotalNum();

    int batchDelete(Integer[] ids);

    List<CartVo> getCarInfoByIds(Integer [] ids);
}
