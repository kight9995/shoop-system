package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.District;
import com.zxst.shoop.mapper.DistrictMapper;
import com.zxst.shoop.service.DistrictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Resource
    private DistrictMapper districtMapper;
    
    @Override
    public List<District> showProDis(Integer parent) {
        return districtMapper.showProDis(parent);
    }
}
