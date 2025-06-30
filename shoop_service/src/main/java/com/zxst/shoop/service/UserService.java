package com.zxst.shoop.service;

import com.zxst.shoop.entity.User;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;

public interface UserService {
    boolean checkUserName(String uname);

    boolean register(User user);

    User login(User user);

    boolean checkoldVal(String oldval, Integer userId);

    boolean modifiedPass(String ps, Integer userId);

    User showOneUser(Integer userId);

    boolean eeditUserInfo(User user);

    //永魂数据分页展示
    PageResult findPage(QueryPageBean queryPageBean);
}
