package com.zxst.shoop.service;

import com.zxst.shoop.entity.Role;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;

public interface RoleService {
    PageResult findPage(QueryPageBean queryPageBean);

    boolean addRoleInfo(Integer[] ids, Role role);

    JsonResult getRoleAndRoleMenu(Integer roleid);
}
