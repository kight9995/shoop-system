package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {

    int saveOrder(Order order);

    List<Order> showOrderInfoByUid(@Param("uid") Integer userId);

    List<Order> findPage(@Param("search") String queryString);

    int deleteInfoByOid(@Param("oid") String oid);

    Order getOrderByOid(@Param("oid") String oid);

    @Select("select count(*) from  t_order where created_time=#{ct}")
    Integer getTodayOrder(@Param("ct") String todayStr);

    @Select("select count(*) from  t_order where created_time between #{start} and #{end}")
    Integer getWeekOrder(@Param("start") String start,@Param("end") String end);

}
