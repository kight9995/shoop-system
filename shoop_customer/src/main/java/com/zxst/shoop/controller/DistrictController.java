package com.zxst.shoop.controller;

import com.zxst.shoop.entity.District;
import com.zxst.shoop.service.DistrictService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/district")
public class DistrictController {

    @Resource
    private DistrictService districtService;

    @RequestMapping("/getParent")
    public List<District> showProDis(Integer parent){
        return  districtService.showProDis(parent);
    }
}
