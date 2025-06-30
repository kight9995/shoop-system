package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Role;
import com.zxst.shoop.service.RoleService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return  roleService.findPage(queryPageBean);
    }

    //添加角色 编辑角色
    @RequestMapping("/addRoleInfo")
    public JsonResult addRoleInfo(Integer[] ids,@RequestBody Role role) {
        System.out.println(Arrays.asList(ids));
        role.setStatus(1);
        boolean flag = roleService.addRoleInfo(ids,role);
        if (flag) {
            return new JsonResult(200,"成功");
        }else{
            return new JsonResult(500,"失败");
        }
    }
    //编辑角色回显信息
    @RequestMapping("/getRoleAndRoleMenu")
    public JsonResult getRoleAndRoleMenu(Integer roleid) {
       return  roleService.getRoleAndRoleMenu(roleid);
    }

}
