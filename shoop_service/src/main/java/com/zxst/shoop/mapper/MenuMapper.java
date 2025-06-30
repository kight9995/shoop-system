package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper {

    List<Menu> getInfoByPid(@Param("pid") Integer  pid);

    @Select("select id from t_menu where link_url is not null and id in(SELECT per_fk FROM `role_menu` where role_fk=#{roleid})")
    List<Integer> getIdByRoleId(@Param("roleid") Integer roleid);

    //查询二级菜单
    @Select("select * from  t_menu where id in(\n" +
            "select per_fk from role_menu where role_fk in(\n" +
            "select role_id from role_user where user_id=#{uid})) and pid=13;")
    List<Menu> getMenuByUid(@Param("uid") Integer userId);

    //查询三级菜单
    @Select("select * from  t_menu where id in(\n" +
            "select per_fk from role_menu where role_fk in(\n" +
            "select role_id from role_user where user_id=#{uid})) and pid=#{pid}")
    List<Menu> getChildMenu(@Param("pid") Integer pid, @Param("uid")  Integer userId);
}
