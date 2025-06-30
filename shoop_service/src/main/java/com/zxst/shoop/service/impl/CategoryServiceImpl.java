package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Category;
import com.zxst.shoop.mapper.CategoryMapper;
import com.zxst.shoop.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> showFirstMenu(Integer pid) {
        return  categoryMapper.showFirstMenu(pid);
    }
}
