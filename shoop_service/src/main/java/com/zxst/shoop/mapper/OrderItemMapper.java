package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderItemMapper {
    int saveOrderItem(OrderItem orderItem);

    List<OrderItem> showItemByOid(@Param("oid") String oid);

    int deleteInfoByOid(@Param("oid") String oid);

    Integer getCountByPid(@Param("pid") Integer id);

    //热门商品查询
    @Select("select any_value(oi.title) name,sum(oi.num) setmeal_count, sum(oi.num)/(select sum(num) from t_order_item) proportion ,p.sell_point from t_order_item oi inner join t_product p on oi.pid=p.id  group by oi.pid order by proportion desc  limit 0,2;")
    public List<Map<String,Object>> getHotProduct();
}
