package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

public interface RoleMapper {
    List<Role> findPage(@Param("search") String queryString);

    int saveRole(Role role);

    Role getRoleByPk(@Param("roleid") Integer roleid);

    int updateById(Role role);
}
