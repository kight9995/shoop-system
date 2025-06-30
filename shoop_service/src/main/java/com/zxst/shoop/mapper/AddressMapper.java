package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressMapper {
    List<Address> showAddress(@Param("uid") Integer userId);

    int updateByUid(@Param("uid") Integer userId);

    int updateByAid(@Param("aid") Integer aid);

    int getMaxCountByUid(@Param("uid") Integer userId);

    int saveAddress(Address address);

    Address getAddressByAid(@Param("aid") Integer sendAddress);
}
