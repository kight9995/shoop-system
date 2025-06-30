package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;

public interface RoleMenuMapper {


    int saveRoleMenu(RoleMenu roleMenu);

    int deleteInfoByRoleId(@Param("roleid") Integer roleid);

}
