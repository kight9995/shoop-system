package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface UserMapper {
    User getUserByUserName(@Param("uname") String uname);

    int register(User user);

    User findUserById(@Param("uid") Integer userId);

    int updateInfo(User user);

    List<User> findPage(@Param("search") String queryString);

    int getCtByDate(@Param("start") String start, @Param("end")String end);

    //今日新增会员数量
    @Select("select count(*) from  t_user where created_time=#{td}")
    Integer getTodayNewMember(@Param("td") String todayStr);
    //总会员数量
    @Select("select count(*) from  t_user")
    Integer getTotalMember();

    //本周新增  本月新增
    @Select("select count(*) from  t_user where created_time between #{startTime} and #{entTime}")
    Integer getNewMemberByTime(@Param("startTime") Date startTime , @Param("entTime") Date endTime);
}
