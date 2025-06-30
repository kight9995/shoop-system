package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Category;
import com.zxst.shoop.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController  extends BaseController{

    @Resource
    private CategoryService categoryService;

    /**
     * 根据类别的上级id查询下一级类别信息
     * @param pid
     * @return
     */
    @RequestMapping("/showFirstMenu")
    public List<Category> showFirstMenu(Integer pid){
        return  categoryService.showFirstMenu(pid);
    }
}
