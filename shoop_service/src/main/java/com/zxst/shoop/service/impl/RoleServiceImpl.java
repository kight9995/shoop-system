package com.zxst.shoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxst.shoop.entity.Role;
import com.zxst.shoop.entity.RoleMenu;
import com.zxst.shoop.mapper.MenuMapper;
import com.zxst.shoop.mapper.RoleMapper;
import com.zxst.shoop.mapper.RoleMenuMapper;
import com.zxst.shoop.service.RoleService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Role> roleList = roleMapper.findPage(queryPageBean.getQueryString());
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Transactional
    @Override
    public boolean addRoleInfo(Integer[] ids, Role role) {
        boolean flag = false;
        if(role.getRoleid() == null){
            //添加角色
            flag =  roleMapper.saveRole(role)>0;
        }else{
            //更新角色信息
            System.out.println("更新动作");
            //更新主表数据
            flag = roleMapper.updateById(role)>0;
            //删除表中原来的角色所有勾选内容
            flag = roleMenuMapper.deleteInfoByRoleId(role.getRoleid())>0;
        }
        //向中间关系表添加数据
        for (Integer id : ids) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleFk(role.getRoleid());
            roleMenu.setPerFk(id);
            flag = roleMenuMapper.saveRoleMenu(roleMenu)>0;
        }
        return flag;
    }



    //回显角色信息
    @Override
    public JsonResult getRoleAndRoleMenu(Integer roleid) {
        //查询角色信息
        Role role = roleMapper.getRoleByPk(roleid);
        //角色对应的菜单的id(功能菜单  url地址不为 null)
        List<Integer> ckid = menuMapper.getIdByRoleId(roleid);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("role",role);
        map.put("ckid",ckid);
        return new JsonResult(200,null,map);
    }
}
