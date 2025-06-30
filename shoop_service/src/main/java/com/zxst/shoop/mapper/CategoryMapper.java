package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    List<Category> showFirstMenu(@Param("pid") Integer pid);
}
