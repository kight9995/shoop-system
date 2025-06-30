package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Address;
import com.zxst.shoop.mapper.AddressMapper;
import com.zxst.shoop.mapper.DistrictMapper;
import com.zxst.shoop.service.AddressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Value("${user.address.max-count}")
    private int maxCount;

    @Resource
    private DistrictMapper districtMapper;

    @Override
    public List<Address> showAddress(Integer userId) {
        return addressMapper.showAddress(userId);
    }
    //更新 1: 将改用户所有地址is_default=0
    // 2: 将传递aid所在的那一条数据的is_default=1;

    @Transactional
    @Override
    public boolean setDefaultAddr(Integer aid, Integer userId) {
        boolean flag = false;
        //1: 将改用户所有地址is_default=0
        flag = addressMapper.updateByUid(userId)>0;
        //设置默认地址
        //将传递aid所在的那一条数据的is_default=1;
        flag = addressMapper.updateByAid(aid)>0;
        return flag;
    }
    //给用户添加地址
    @Override
    public boolean saveAddress(Integer userId, String userName, Address address) {
        //检查该用户的地址个数是否超标
        int ct = addressMapper.getMaxCountByUid(userId);
        if (ct<10){
            //地址添加
            //根据地址编号 获取地址名称
            String ProName = districtMapper.getDistrictNameByCode(address.getProvinceCode());
            String cityName = districtMapper.getDistrictNameByCode(address.getCityCode());
            String areaName = districtMapper.getDistrictNameByCode(address.getAreaCode());
            //是否是该用户的默认地址
            int isDefault = ct == 0?1:0;
            //给对象赋值
            address.setUid(userId);
            address.setName(userName);
            address.setProvinceName(ProName);
            address.setCityName(cityName);
            address.setAreaName(areaName);
            address.setIsDefault(isDefault);
            address.setCreatedTime(java.util.Calendar.getInstance().getTime());
            address.setModifiedTime(java.util.Calendar.getInstance().getTime());
            address.setModifiedUser(userName);
            address.setCreatedUser(userName);
            return  addressMapper.saveAddress(address)>0;
        }else{
            return false;
        }
    }

    @Override
    public Address getAddressByAid(Integer sendAddress) {
        return addressMapper.getAddressByAid(sendAddress);
    }
}
