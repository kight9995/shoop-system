package com.zxst.shoop.service;

import com.zxst.shoop.util.JsonResult;

public interface MenuService {
    JsonResult showAllMenu();

    JsonResult showMenuByUid(Integer userId);
}
