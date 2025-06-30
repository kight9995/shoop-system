package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.District;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DistrictMapper {
    List<District> showProDis(@Param("parent") Integer parent);

    String getDistrictNameByCode(@Param("code") String areaCode);
}
