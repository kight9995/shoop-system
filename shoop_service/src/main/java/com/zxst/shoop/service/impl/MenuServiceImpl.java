package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Menu;
import com.zxst.shoop.mapper.MenuMapper;
import com.zxst.shoop.service.MenuService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public JsonResult showAllMenu() {
        //第一级别菜单 pid  is  null
        List<Menu> first = menuMapper.getInfoByPid(null);
        for (Menu menu : first) {
            //查询指定pid的二级菜单
            List<Menu> second = menuMapper.getInfoByPid(menu.getId());
            for (Menu menu1 : second) {
                //查询pid的三级菜单
                List<Menu> third = menuMapper.getInfoByPid(menu1.getId());
                menu1.setChildren(third);
            }
            //将二级菜单设置为一级菜单子级
            menu.setChildren(second);
        }
        return new JsonResult(200,null,first);
    }


    @Override
    public JsonResult showMenuByUid(Integer userId) {
        //父级列表  用户id
        List<Menu> second = menuMapper.getMenuByUid(userId);
        for (Menu menu : second) {
            List<Menu> third = menuMapper.getChildMenu(menu.getId(),userId);
            menu.setChildren(third);
        }
        return new JsonResult(200,null,second);
    }
}
